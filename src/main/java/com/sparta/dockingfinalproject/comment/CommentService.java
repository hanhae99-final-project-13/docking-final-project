package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  //Comment 등록
  @Transactional
  public Map<String, Object> addComment(Long postId, CommentRequestDto commentRequestDto,
      UserDetailsImpl userDetails) {
    //User 가져오기
    Long userId = userDetails.getUser().getUserId();
    User user = userRepository.getById(userId);

    //해당 Post 가져오기
    Post post = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    //db에 Comment 저장
    Comment comment = new Comment(post, commentRequestDto, user);

    List<Comment> postCommentList = post.getCommentList();

    if (!postCommentList.contains(comment)) {
      postCommentList.add(comment);
    }

    commentRepository.save(comment);
    System.out.println(comment);

    //리턴 data 생성
    Map<String, Object> data = new HashMap<>();
    data.put("msg", "댓글이 등록 되었습니다");
    return SuccessResult.success(data);

  }

  //Comment 수정
  @Transactional
  public Map<String, Object> updateComment(Long commentId, CommentRequestDto commentRequestDto) {
    //해당 Comment 가져오기
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    //db에 Comment 업데이트
    comment.update(commentRequestDto);

    //리턴 data 생성
    Map<String, String> data = new HashMap<>();
    data.put("msg", "댓글이 수정 되었습니다");
    return SuccessResult.success(data);
  }

  //Comment 삭제
  @Transactional
  public Map<String, Object> deleteComment(Long commentId) {
    //해당 Comment 가져오기
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    //db에 Comment 삭제
    commentRepository.deleteById(commentId);

    Map<String, Object> data = new HashMap<>();
    data.put("msg", "삭제 완료");
    return SuccessResult.success(data);
  }

}

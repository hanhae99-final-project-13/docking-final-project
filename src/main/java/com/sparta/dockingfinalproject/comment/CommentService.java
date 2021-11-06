package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentEditRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentResultDto;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  //Comment 등록
  @Transactional
  public Map<String, Object> addComment(CommentRequestDto commentRequestDto,
      UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }

    //User 가져오기
    Long userId = userDetails.getUser().getUserId();
    User user = userRepository.getById(userId);

    //해당 Post 가져오기
    Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    //db에 Comment 저장
    Comment comment = new Comment(post, commentRequestDto, user);

    Comment newComment = commentRepository.save(comment);
    Long commentId = newComment.getCommentId();
    String commentContent = newComment.getComment();
    String nickName = newComment.getUser().getNickname();
    LocalDateTime createdAt = newComment.getCreatedAt();
    LocalDateTime modifiedAt = newComment.getModifiedAt();

    CommentResultDto commentResultDto = new CommentResultDto(commentId, commentContent, nickName,
        createdAt, modifiedAt);

    //리턴 data 생성
    Map<String, Object> data = new HashMap<>();
    data.put("msg", "댓글이 등록 되었습니다");
    data.put("newComment", commentResultDto);
    return SuccessResult.success(data);

  }

  //Comment 수정
  @Transactional
  public Map<String, Object> updateComment(Long commentId, CommentEditRequestDto requestDto,
      UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    }

    //해당 Comment 가져오기
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
    Long userId = userDetails.getUser().getUserId();
    Long writerId = comment.getUser().getUserId();

    //db에 Comment 업데이트
    Map<String, String> data = new HashMap<>();
    if (userId.equals(writerId)) {
      comment.update(requestDto);
      data.put("msg", "댓글이 수정 되었습니다");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  //Comment 삭제
  @Transactional
  public Map<String, Object> deleteComment(Long commentId, UserDetailsImpl userDetails) {
    //해당 Comment 가져오기
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
    Long userId = userDetails.getUser().getUserId();
    Long writerId = comment.getUser().getUserId();

    //db에 Comment 삭제
    Map<String, Object> data = new HashMap<>();
    if (userId.equals(writerId)) {
      commentRepository.deleteById(commentId);
      data.put("msg", "삭제 완료");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

}

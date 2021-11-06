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

  //Comment 등록
  @Transactional
  public Map<String, Object> addComment(CommentRequestDto commentRequestDto,
      UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.LOGIN_REQUIRED);
    }

    User user = userDetails.getUser();
    Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    Comment comment = new Comment(post, commentRequestDto, user);
    Comment newComment = commentRepository.save(comment);
    CommentResultDto commentResultDto = CommentResultDto.of(newComment);

    Map<String, Object> data = new HashMap<>();
    data.put("msg", "댓글이 등록 되었습니다");
    data.put("newComment", commentResultDto);
    return SuccessResult.success(data);

  }

  //Comment 수정
  public Map<String, Object> updateComment(Long commentId, CommentEditRequestDto requestDto,
      UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.LOGIN_REQUIRED);
    }

    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new DockingException(ErrorCode.COMMENT_NOT_FOUND)
    );
    Long userId = userDetails.getUser().getUserId();
    Long writerId = comment.getUser().getUserId();

    Map<String, Object> data = new HashMap<>();
    if (userId.equals(writerId)) {
      Comment newComment = comment.update(requestDto);   //객체 변경만 한거...레포지토리 안들렸으니까

      Comment savedNewComment = commentRepository.save(newComment);
      Long updatedCommentId = savedNewComment.getCommentId();

      Comment updatedComment = commentRepository.findById(updatedCommentId).orElseThrow(
          () -> new DockingException(ErrorCode.COMMENT_NOT_FOUND)
      );

      CommentResultDto commentResultDto = CommentResultDto.of(updatedComment);

      data.put("msg", "댓글이 수정 되었습니다");
      data.put("updatedComment", commentResultDto);
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  //Comment 삭제
  @Transactional
  public Map<String, Object> deleteComment(Long commentId, UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.LOGIN_REQUIRED);
    }

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

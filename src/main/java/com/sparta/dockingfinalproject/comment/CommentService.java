package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.comment.dto.CommentEditRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentResultDto;
import com.sparta.dockingfinalproject.comment.model.Comment;
import com.sparta.dockingfinalproject.comment.repository.CommentRepository;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final AlarmRepository alarmRepository;
  private final SimpMessageSendingOperations simpMessageSendingOperations;

  //Comment 등록
  @Transactional
  public Map<String, Object> addComment(CommentRequestDto commentRequestDto,
      UserDetailsImpl userDetails) {
    User user = bringUser(userDetails);
    Post post = bringPost(commentRequestDto);

    Comment comment = new Comment(post, commentRequestDto, user);
    Comment newComment = commentRepository.save(comment);
    CommentResultDto commentResultDto = CommentResultDto.of(newComment);

    if (!user.getNickname().equals(post.getUser().getNickname())) {
      saveCommentAlarm(user.getNickname(), comment.getCommentId(), post.getUser());
      alarmBySocketMessage(post.getUser(), user.getNickname(), newComment.getComment());
    }

    Map<String, Object> data = new HashMap<>();
    data.put("msg", "댓글이 등록 되었습니다");
    data.put("newComment", commentResultDto);
    return SuccessResult.success(data);
  }

  private void saveCommentAlarm(String commentWriter, Long commentId, User user) {
    String alarmContent = commentWriter + "님이 게시글에 댓글을 등록하였습니다";
    AlarmType alarmType = AlarmType.COMMENT;
    Alarm alarm = new Alarm(alarmContent, alarmType, commentId, user);
    alarmRepository.save(alarm);
  }

  private void alarmBySocketMessage(User user, String alarmNickname, String comment) {
    List<Alarm> alarms = alarmRepository
        .findAllByUserAndCheckedTrueOrderByCreatedAtDesc(user);
    Map<String, Object> result = new HashMap<>();
    result.put("alarmCount", alarms.size()+1);
    result.put("alarmNickname", alarmNickname);
    result.put("comment", comment);
    simpMessageSendingOperations.convertAndSend("/sub/" + user.getUserId(), result);
  }

  //Comment 수정
  public Map<String, Object> updateComment(Long commentId, CommentEditRequestDto requestDto,
      UserDetailsImpl userDetails) {
    Long userId = bringUser(userDetails).getUserId();
    Comment comment = bringComment(commentId);
    Long writerId = comment.getUser().getUserId();

    Map<String, Object> data = new HashMap<>();
    if (userId.equals(writerId)) {
      Comment newComment = comment.update(requestDto);
      Comment savedNewComment = commentRepository.save(newComment);
      Long updatedCommentId = savedNewComment.getCommentId();

      Comment updatedComment = bringComment(updatedCommentId);
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
    Long userId = bringUser(userDetails).getUserId();
    Comment comment = bringComment(commentId);
    Long writerId = comment.getUser().getUserId();

    Map<String, Object> data = new HashMap<>();
    if (userId.equals(writerId)) {
      commentRepository.deleteById(commentId);
      alarmRepository.deleteByAlarmTypeAndContentId(AlarmType.COMMENT, commentId);
      data.put("msg", "삭제 완료");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  //로그인 체크 & User 가져오기
  private User bringUser(UserDetailsImpl userDetails) {
    if (userDetails != null) {
      return userDetails.getUser();
    } else {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    }
  }

  //해당 Post 가져오기
  private Post bringPost(CommentRequestDto commentRequestDto) {
    return postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
  }

  //해당 Comment 가져오기
  private Comment bringComment(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new DockingException(ErrorCode.COMMENT_NOT_FOUND)
    );
  }

}


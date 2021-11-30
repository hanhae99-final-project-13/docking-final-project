package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.comment.model.Comment;
import com.sparta.dockingfinalproject.comment.repository.CommentRepository;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlarmService {

  private final AlarmRepository alarmRepository;
  private final CommentRepository commentRepository;

  public AlarmService(AlarmRepository alarmRepository, CommentRepository commentRepository) {
    this.alarmRepository = alarmRepository;
    this.commentRepository = commentRepository;
  }

  public Map<String, Object> getAlarms(User user) {
    List<Alarm> alarms = alarmRepository.findAllByUserOrderByCreatedAtDesc(user);

    Map<String, Object> data = new HashMap<>();
    data.put("data", getResponseAlarms(alarms));
    data.put("alarmCount", getAlarmCount(user));
    return SuccessResult.success(data);
  }

  private List<AlarmResponseDto> getResponseAlarms(List<Alarm> alarms) {
    List<AlarmResponseDto> dataAlarms = new ArrayList<>();
    for (Alarm alarm : alarms) {
      AlarmResponseDto alarmResponseDto = getAlarmResponseDto(alarm);
      dataAlarms.add(alarmResponseDto);
    }
    return dataAlarms;
  }

  public int getAlarmCount(User user) {
    List<Alarm> alarms = alarmRepository.findAllByUserAndCheckedTrueOrderByCreatedAtDesc(user);
    return alarms.size();
  }

  @Transactional
  public Map<String, Object> deleteAlarms(User user) {
    alarmRepository.deleteByUser(user);

    Map<String, String> data = new HashMap<>();
    data.put("msg", "삭제 완료되었습니다.");

    return SuccessResult.success(data);
  }

  public Map<String, Object> getAlarm(Long alarmId, User user) {
    Alarm alarm = findAlarm(alarmId);
    modifyAlarmStatus(alarm);

    Map<String, Object> data = new HashMap<>();
    data.put("data", getAlarmResponseDto(alarm));
    data.put("alarmCount", getAlarmCount(user));
    return SuccessResult.success(data);
  }

  private Alarm findAlarm(Long alarmId) {
    return alarmRepository.findById(alarmId).orElseThrow(
        () -> new DockingException(ErrorCode.ALARM_NOT_FOUND)
    );
  }

  private void modifyAlarmStatus(Alarm alarm) {
    alarm.updateIsRead();
    alarmRepository.save(alarm);
  }

  private AlarmResponseDto getAlarmResponseDto(Alarm alarm) {
    AlarmType alarmType = alarm.getAlarmType();

    String commentContent = null;
    Long postId = null;
    if (alarmType == AlarmType.COMMENT) {
      Comment comment = getComment(alarm.getContentId());
      commentContent = comment.getComment();
      postId = comment.getPost().getPostId();
    }

    return AlarmResponseDto.of(alarm, postId, commentContent);
  }

  private Comment getComment(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new DockingException(ErrorCode.COMMENT_NOT_FOUND));
  }

//  private String validAlarmType(Alarm alarm, AlarmType alarmType) {
//    String contentDetail;
//    if (alarmType == AlarmType.COMMENT) {
//      Long commentId = alarm.getContentId();
//      Comment comment = commentRepository.findById(commentId).orElseThrow(
//          () -> new DockingException(ErrorCode.COMMENT_NOT_FOUND));
//      Long postId = comment.getPost().getPostId();
//      contentDetail = "[postId:" + postId + "] [comment:" + comment.getComment() + "]";
//    } else {
//      contentDetail = null;
//    }
//    return contentDetail;
//  }
}

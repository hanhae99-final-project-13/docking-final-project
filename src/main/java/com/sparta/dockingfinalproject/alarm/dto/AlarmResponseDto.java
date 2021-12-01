package com.sparta.dockingfinalproject.alarm.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AlarmResponseDto {

  private Long alarmId;
  private String alarmContent;
  private boolean checked;
  private AlarmType alarmType;
  private Long contentId;
  private Long postId;
  private String comment;
  private LocalDateTime createdAt;

  @QueryProjection
  public AlarmResponseDto(Long alarmId, String alarmContent, boolean checked, AlarmType alarmType, Long contentId, LocalDateTime createdAt) {
    this.alarmId = alarmId;
    this.alarmContent = alarmContent;
    this.checked = checked;
    this.alarmType = alarmType;
    this.contentId = contentId;
    this.createdAt = createdAt;
    this.postId = null;
    this.comment = null;
  }

  public AlarmResponseDto(Long alarmId, String alarmContent, boolean checked, AlarmType alarmType,
      Long contentId, Long postId, String comment, LocalDateTime createdAt) {
    this.alarmId = alarmId;
    this.alarmContent = alarmContent;
    this.checked = checked;
    this.alarmType = alarmType;
    this.postId = postId;
    this.createdAt = createdAt;
  }

  public void addComment(String comment, Long postId) {
    this.postId = postId;
    this.comment = comment;
  }

  public static AlarmResponseDto of(Alarm alarm, Long postId, String comment) {
    return new AlarmResponseDto(alarm.getAlarmId(), alarm.getAlarmContent(), alarm.isChecked(), alarm.getAlarmType(),
        alarm.getContentId(), postId, comment, alarm.getCreatedAt());
  }
}

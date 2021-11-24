package com.sparta.dockingfinalproject.alarm.dto;

import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmResponseDto {

  private Long alarmId;
  private String alarmContent;
  private boolean checked;
  private AlarmType alarmType;
  private Long contentId;
  private String contentDetail;
  private LocalDateTime createdAt;


  public static AlarmResponseDto of(Alarm alarm, String contentDetail) {
    return AlarmResponseDto.builder()
        .alarmId(alarm.getAlarmId())
        .alarmContent(alarm.getAlarmContent())
        .checked(alarm.isChecked())
        .alarmType(alarm.getAlarmType())
        .contentId(alarm.getContentId())
        .contentDetail(contentDetail)
        .createdAt(alarm.getCreatedAt())
        .build();
  }
}

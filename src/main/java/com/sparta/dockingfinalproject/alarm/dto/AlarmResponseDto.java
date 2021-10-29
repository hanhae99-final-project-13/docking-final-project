package com.sparta.dockingfinalproject.alarm.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmResponseDto {
  private Long alarmId;
  private String alarmContent;
}

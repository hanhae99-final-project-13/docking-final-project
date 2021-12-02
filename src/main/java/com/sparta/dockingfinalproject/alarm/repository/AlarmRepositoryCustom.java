package com.sparta.dockingfinalproject.alarm.repository;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import java.util.Optional;

public interface AlarmRepositoryCustom {

  List<AlarmResponseDto> findAllUserAlarm(User user);
  Optional<AlarmResponseDto> findUserAlarm(Long alarmId);
}

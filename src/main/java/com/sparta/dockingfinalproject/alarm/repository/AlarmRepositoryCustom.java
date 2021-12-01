package com.sparta.dockingfinalproject.alarm.repository;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.user.User;
import java.util.List;

public interface AlarmRepositoryCustom {

  List<AlarmResponseDto> findAllUserAlarm(User user);
}

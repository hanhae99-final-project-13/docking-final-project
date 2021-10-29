package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {

  private final AlarmRepositoroy alarmRepositoroy;

  public AlarmService(AlarmRepositoroy alarmRepositoroy) {
    this.alarmRepositoroy = alarmRepositoroy;
  }

  public Map<String, Object> getAlarms(UserDetailsImpl userDetails) {
    List<Alarm> alarms = alarmRepositoroy.findAllByUserAndStatusTrueOrderByCreatedAtDesc(userDetails.getUser());

    List<AlarmResponseDto> data = new ArrayList<>();
    for (Alarm alarm : alarms) {
      AlarmResponseDto alarmResponseDto = AlarmResponseDto.builder()
                                                      .alarmId(alarm.getAlarmId())
                                                      .alarmContent(alarm.getAlarmContent())
                                                      .build();
      data.add(alarmResponseDto);
    }

    return SuccessResult.success(data);
  }

  public int getAlarmCount(User user) {
    List<Alarm> alarms = alarmRepositoroy.findAllByUserAndStatusTrueOrderByCreatedAtDesc(user);
    return alarms.size();
  }
}

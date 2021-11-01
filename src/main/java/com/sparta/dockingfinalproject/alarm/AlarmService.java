package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import java.util.ArrayList;
import java.util.HashMap;
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
    System.out.println(userDetails.getUser().getUsername());
    List<Alarm> alarms = alarmRepositoroy.findAllByUserOrderByCreatedAtDesc(userDetails.getUser());

    List<AlarmResponseDto> dataAlarms = new ArrayList<>();
    for (Alarm alarm : alarms) {
      AlarmResponseDto alarmResponseDto = AlarmResponseDto.builder()
                                                      .alarmId(alarm.getAlarmId())
                                                      .alarmContent(alarm.getAlarmContent())
                                                      .build();
      dataAlarms.add(alarmResponseDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("data", dataAlarms);
    data.put("alarmCount", getAlarmCount(userDetails.getUser()));

    return SuccessResult.success(data);
  }

  public int getAlarmCount(User user) {
    List<Alarm> alarms = alarmRepositoroy.findAllByUserAndStatusTrueOrderByCreatedAtDesc(user);
    return alarms.size();
  }

  public Map<String, Object> deleteAlarms() {
    alarmRepositoroy.deleteAll();

    Map<String, String> data = new HashMap<>();
    data.put("msg", "삭제 완료되었습니다.");

    return SuccessResult.success(data);
  }

  public Map<String, Object> getAlarm(Long alarmId, UserDetailsImpl userDetails) {
    Alarm alarm = alarmRepositoroy.findById(alarmId).orElseThrow(
        () -> new DockingException(ErrorCode.ALARM_NOT_FOUND)
    );

    alarm.updateStatus();
    alarmRepositoroy.save(alarm);

    AlarmResponseDto dataAlarm = AlarmResponseDto.builder()
                                      .alarmId(alarm.getAlarmId())
                                      .alarmContent(alarm.getAlarmContent())
                                      .build();

    Map<String, Object> data = new HashMap<>();
    data.put("data", dataAlarm);
    data.put("alarmCount", getAlarmCount(userDetails.getUser()));
    return SuccessResult.success(data);
  }
}

package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {

  private final AlarmRepository alarmRepository;

  public AlarmService(AlarmRepository alarmRepository) {
    this.alarmRepository = alarmRepository;
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
    List<Alarm> alarms = alarmRepository.findAllByUserAndStatusTrueOrderByCreatedAtDesc(user);
    return alarms.size();
  }

  public Map<String, Object> deleteAlarms(User user) {
    alarmRepository.deleteAllByUser(user);

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
    alarm.updateStatus();
    alarmRepository.save(alarm);
  }

  private AlarmResponseDto getAlarmResponseDto(Alarm alarm) {
    return AlarmResponseDto.builder()
        .alarmId(alarm.getAlarmId())
        .alarmContent(alarm.getAlarmContent())
        .build();
  }
}

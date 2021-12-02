package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.user.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlarmService {

  private final AlarmRepository alarmRepository;

  public AlarmService(AlarmRepository alarmRepository) {
    this.alarmRepository = alarmRepository;
  }

  public Map<String, Object> getAlarms(User user) {
    Map<String, Object> data = new HashMap<>();
    data.put("data", alarmRepository.findAllUserAlarm(user));
    data.put("alarmCount", getAlarmCount(user));
    return SuccessResult.success(data);
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

    AlarmResponseDto alarmResponseDto = alarmRepository.findUserAlarm(alarmId).orElseThrow(
        () -> new DockingException(ErrorCode.ALARM_NOT_FOUND)
    );

    Map<String, Object> data = new HashMap<>();
    data.put("data", alarmResponseDto);
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
}

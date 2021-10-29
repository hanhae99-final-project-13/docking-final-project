package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlarmController {

  private final AlarmService alarmService;

  public AlarmController(AlarmService alarmService) {
    this.alarmService = alarmService;
  }

  @GetMapping("/alarms")
  public Map<String, Object> getAlarms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return alarmService.getAlarms(userDetails);
  }

  @GetMapping("/alarm/{alarmId}")
  public Map<String, Object> getAlarm(@PathVariable Long alarmId) {
    return alarmService.getAlarm(alarmId);
  }
}

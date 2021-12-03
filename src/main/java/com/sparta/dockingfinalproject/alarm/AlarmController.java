package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlarmController {

  private final AlarmService alarmService;
  private final SimpMessageSendingOperations simpMessageSendingOperations;

  public AlarmController(AlarmService alarmService, SimpMessageSendingOperations simpMessageSendingOperations) {
    this.alarmService = alarmService;
    this.simpMessageSendingOperations = simpMessageSendingOperations;
  }

  @GetMapping("/alarms")
  public ResponseEntity<Map<String, Object>> getAlarms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(alarmService.getAlarms(userDetails.getUser()));
  }

  @DeleteMapping("/alarms")
  public ResponseEntity<Map<String, Object>> deleteAlarms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(alarmService.deleteAlarms(userDetails.getUser()));
  }

  @GetMapping("/alarms/{alarmId}")
  public ResponseEntity<Map<String, Object>> getAlarm(@PathVariable Long alarmId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(alarmService.getAlarm(alarmId, userDetails.getUser()));
  }

  @MessageMapping("/{userId}")
  public void realAlarm(@DestinationVariable("userId") Long userId) {
    simpMessageSendingOperations.convertAndSend("/sub/" + userId, "alarm connection test");
  }
}

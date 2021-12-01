package com.sparta.dockingfinalproject.alarm.repository;

import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
  List<Alarm> findAllByUserOrderByCreatedAtDesc(User user);
  List<Alarm> findAllByUserAndCheckedTrueOrderByCreatedAtDesc(User user);
  void deleteByUser(User user);
  void deleteByAlarmTypeAndContentId(AlarmType alarmType, Long contentId);
}

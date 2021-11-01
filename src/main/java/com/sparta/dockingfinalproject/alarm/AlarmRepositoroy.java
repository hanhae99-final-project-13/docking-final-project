package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepositoroy extends JpaRepository<Alarm, Long> {
  List<Alarm> findAllByUserAndStatusTrueOrderByCreatedAtDesc(User user);
  List<Alarm> findAllByUserOrderByCreatedAtDesc(User user);
}

package com.sparta.dockingfinalproject.alarm;

import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
//  List<Alarm> findAllByUserAndStatusTrueOrderByCreatedAtDesc(User user);
  List<Alarm> findAllByUserOrderByCreatedAtDesc(User user);
  List<Alarm> findAllByUserAndStatusTrueOrderByCreatedAtDesc(User user);
  void deleteAllByUser(User user);
}

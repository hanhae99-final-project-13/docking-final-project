package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FosterFormRepository extends JpaRepository<FosterForm, Long> {

  List<FosterForm> findAllByUser(User user);

  List<FosterForm> findAllByCreatedAtBetweenOrderByCreatedAt(LocalDateTime start, LocalDateTime end);
}

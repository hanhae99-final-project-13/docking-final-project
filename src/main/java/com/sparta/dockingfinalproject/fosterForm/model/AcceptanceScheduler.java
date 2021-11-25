package com.sparta.dockingfinalproject.fosterForm.model;

import com.sparta.dockingfinalproject.fosterForm.FosterFormRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AcceptanceScheduler {

  private final FosterFormRepository fosterFormRepository;

  public AcceptanceScheduler(FosterFormRepository fosterFormRepository) {
    this.fosterFormRepository = fosterFormRepository;
  }

  @Scheduled(cron = "0 0 0/1 * * *")
  public void updateOveredFosterForms() {
    System.out.println("입양신청 1일 경과 FosterForm 상태변경");
    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().minusDays(10),
        LocalTime.of(0, 0, 0));
    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59));
    List<FosterForm> fosterFormList = fosterFormRepository.findAllByCreatedAtBetweenOrderByCreatedAt(
        startDateTime, endDateTime);

    for (FosterForm fosterForm : fosterFormList) {
      fosterForm.updateAcceptance(Acceptance.of("rejected"));
    }
  }

}

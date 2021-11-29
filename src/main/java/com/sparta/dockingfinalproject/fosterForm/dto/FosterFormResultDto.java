package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.fosterForm.model.Acceptance;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.pet.Sex;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FosterFormResultDto {

  private Long fosterFormId;
  private String name;
  private Long fosterAge;
  private Sex gender;
  private String phone;
  private String job;
  private String fosterAddress;
  private String currentPet;
  private String experience;
  private String reason;
  private String allergy;
  private String family;
  private String timeTogether;
  private String anxiety;
  private String bark;
  private String leaveHome;
  private String medicalBudget;
  private String monthlyBudget;
  private String roomUrl;
  private String signUrl;
  private String etc;
  private Acceptance acceptance;
  private Long postId;
  private Map<String, Object> eduStatus;

  public static FosterFormResultDto of(FosterForm fosterForm, Map<String, Object> eduStatus) {
    return FosterFormResultDto.builder()
        .fosterFormId(fosterForm.getFosterFormId())
        .name(fosterForm.getName())
        .fosterAge(fosterForm.getFosterAge())
        .gender(fosterForm.getGender())
        .phone(fosterForm.getPhone())
        .job(fosterForm.getJob())
        .fosterAddress(fosterForm.getFosterAddress())
        .currentPet(fosterForm.getCurrentPet())
        .experience(fosterForm.getExperience())
        .reason(fosterForm.getReason())
        .allergy(fosterForm.getAllergy())
        .family(fosterForm.getFamily())
        .timeTogether(fosterForm.getTimeTogether())
        .anxiety(fosterForm.getAnxiety())
        .bark(fosterForm.getBark())
        .leaveHome(fosterForm.getLeaveHome())
        .medicalBudget(fosterForm.getMedicalBudget())
        .monthlyBudget(fosterForm.getMonthlyBudget())
        .roomUrl(fosterForm.getRoomUrl())
        .signUrl(fosterForm.getSignUrl())
        .etc(fosterForm.getEtc())
        .acceptance(fosterForm.getAcceptance())
        .postId(fosterForm.getPost().getPostId())
        .eduStatus(eduStatus)
        .build();
  }

}
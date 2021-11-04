package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.fosterForm.FosterForm;
import com.sparta.dockingfinalproject.pet.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FosterFormResultDto {

  private Long fosterFormId;
  private String name;
  private Long birthYear;
  private Sex gender;
  private String phone;
  private String job;
  private String fosterAddress;
  private String currentPet;
  private String experience;
  private String reason;
  private String family;
  private String timeTogether;
  private String anxiety;
  private String bark;
  private String roomUrl;
  private Long postId;

  public static FosterFormResultDto of(FosterForm fosterForm) {
    return FosterFormResultDto.builder()
        .fosterFormId(fosterForm.getFosterFormId())
        .name(fosterForm.getName())
        .birthYear(fosterForm.getBirthYear())
        .gender(fosterForm.getGender())
        .phone(fosterForm.getPhone())
        .job(fosterForm.getJob())
        .fosterAddress(fosterForm.getFosterAddress())
        .currentPet(fosterForm.getCurrentPet())
        .experience(fosterForm.getExperience())
        .reason(fosterForm.getReason())
        .family(fosterForm.getFamily())
        .timeTogether(fosterForm.getTimeTogether())
        .anxiety(fosterForm.getAnxiety())
        .bark(fosterForm.getBark())
        .roomUrl(fosterForm.getRoomUrl())
        .postId(fosterForm.getPost().getPostId())
        .build();
  }

}
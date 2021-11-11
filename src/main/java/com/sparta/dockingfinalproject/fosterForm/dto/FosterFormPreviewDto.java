package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.fosterForm.Acceptance;
import com.sparta.dockingfinalproject.fosterForm.FosterForm;
import com.sparta.dockingfinalproject.pet.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FosterFormPreviewDto {

  private Long fosterFormId;
  private String name;
  private Long fosterAge;
  private Sex gender;
  private String phone;
  private String fosterAddress;
  private Acceptance acceptance;

  public static FosterFormPreviewDto of(FosterForm fosterForm) {
    return FosterFormPreviewDto.builder()
        .fosterFormId(fosterForm.getFosterFormId())
        .name(fosterForm.getName())
        .fosterAge(fosterForm.getFosterAge())
        .gender(fosterForm.getGender())
        .phone(fosterForm.getPhone())
        .fosterAddress(fosterForm.getFosterAddress())
        .acceptance(fosterForm.getAcceptance())
        .build();
  }
}

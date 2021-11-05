package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.fosterForm.FosterForm;
import com.sparta.dockingfinalproject.pet.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FormPreviewDto {

  private Long fosterFormId;
  private String name;
  private Long fosterAge;
  private Sex gender;
  private String phone;
  private String fosterAddress;

  public static FormPreviewDto of(FosterForm fosterForm) {
    return FormPreviewDto.builder()
        .fosterFormId(fosterForm.getFosterFormId())
        .name(fosterForm.getName())
        .fosterAge(fosterForm.getFosterAge())
        .gender(fosterForm.getGender())
        .phone(fosterForm.getPhone())
        .fosterAddress(fosterForm.getFosterAddress())
        .build();
  }
}

package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.fosterForm.model.Acceptance;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.post.pet.model.Sex;
import java.util.Map;
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
  private Map<String, Object> eduStatus;

  public static FosterFormPreviewDto of(FosterForm fosterForm, Map<String, Object> eduStatus) {
    return FosterFormPreviewDto.builder()
        .fosterFormId(fosterForm.getFosterFormId())
        .name(fosterForm.getName())
        .fosterAge(fosterForm.getFosterAge())
        .gender(fosterForm.getGender())
        .phone(fosterForm.getPhone())
        .fosterAddress(fosterForm.getFosterAddress())
        .acceptance(fosterForm.getAcceptance())
        .eduStatus(eduStatus)
        .build();
  }
}

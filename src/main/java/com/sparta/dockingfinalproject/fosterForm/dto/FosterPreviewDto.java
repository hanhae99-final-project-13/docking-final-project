package com.sparta.dockingfinalproject.fosterForm.dto;


import com.sparta.dockingfinalproject.fosterForm.FosterForm;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.dto.PostPreviewDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FosterPreviewDto {

  private Long fosterFormId;
  private String name;
  private Long fosterAge;
  private Sex gender;
  private String phone;
  private String fosterAddress;
  private PostPreviewDto postPreview;

  public static FosterPreviewDto of(FosterForm fosterForm, PostPreviewDto postPreviewDto) {
    return FosterPreviewDto.builder()
        .fosterFormId(fosterForm.getFosterFormId())
        .name(fosterForm.getName())
        .fosterAge(fosterForm.getFosterAge())
        .gender(fosterForm.getGender())
        .phone(fosterForm.getPhone())
        .fosterAddress(fosterForm.getFosterAddress())
        .postPreview(postPreviewDto)
        .build();
  }

}
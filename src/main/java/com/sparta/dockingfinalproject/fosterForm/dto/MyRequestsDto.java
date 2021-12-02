package com.sparta.dockingfinalproject.fosterForm.dto;


import com.sparta.dockingfinalproject.fosterForm.model.Acceptance;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.post.dto.response.PostPreviewDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyRequestsDto {

  private Long fosterFormId;
  private Acceptance acceptance;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private PostPreviewDto postPreview;

  public static MyRequestsDto of(FosterForm fosterForm, PostPreviewDto postPreviewDto) {
    return MyRequestsDto.builder()
        .fosterFormId(fosterForm.getFosterFormId())
        .acceptance(fosterForm.getAcceptance())
        .createdAt(fosterForm.getCreatedAt())
        .modifiedAt(fosterForm.getModifiedAt())
        .postPreview(postPreviewDto)
        .build();
  }

}
package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.post.dto.PostPreviewDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FormsInPostDto {

  private PostPreviewDto postPreview;
  private List<FormPreviewDto> formPreviews;

  public static FormsInPostDto of(PostPreviewDto postPreviewDto,
      List<FormPreviewDto> formPreviewDtos) {
    return FormsInPostDto.builder()
        .postPreview(postPreviewDto)
        .formPreviews(formPreviewDtos)
        .build();
  }
}
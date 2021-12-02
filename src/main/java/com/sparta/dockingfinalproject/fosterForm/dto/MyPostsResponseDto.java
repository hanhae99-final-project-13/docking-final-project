package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.post.dto.response.PostPreviewDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyPostsResponseDto {

  private PostPreviewDto postPreview;
  private List<FosterFormPreviewDto> formPreviews;

  public static MyPostsResponseDto of(PostPreviewDto postPreviewDto,
      List<FosterFormPreviewDto> fosterFormPreviewDtos) {
    return MyPostsResponseDto.builder()
        .postPreview(postPreviewDto)
        .formPreviews(fosterFormPreviewDtos)
        .build();
  }
}
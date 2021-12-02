package com.sparta.dockingfinalproject.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchRequestDto {

  private String startDt;
  private String endDt;
  private String ownerType;
  private String city;
  private String district;
  private String sort;
}
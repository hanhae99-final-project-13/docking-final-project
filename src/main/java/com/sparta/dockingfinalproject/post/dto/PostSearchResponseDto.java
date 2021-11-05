package com.sparta.dockingfinalproject.post.dto;

import com.sparta.dockingfinalproject.pet.Sex;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSearchResponseDto {
  private Long userId;
  private String nickname;
  private Long postId;
  private String breed;
  private Sex sex;
  private int age;
  private String ownerType;
  private String address;
  private List<String> img;
  private String isAdopted;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}

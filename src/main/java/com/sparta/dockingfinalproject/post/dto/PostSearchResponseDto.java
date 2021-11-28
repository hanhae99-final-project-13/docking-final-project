package com.sparta.dockingfinalproject.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Sex;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchResponseDto {
  private Long userId;
  private String nickname;
  private Long postId;
  private String breed;
  private Sex sex;
  private int age;
  private String ownerType;
  private String address;
  private List<String> img= new ArrayList<>();
  private IsAdopted isAdopted;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  @QueryProjection
  public PostSearchResponseDto(Long userId, String nickname, Long postId, String breed,
      Sex sex, int age, String ownerType, String address, String imgs, IsAdopted isAdopted,
      LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.userId = userId;
    this.nickname = nickname;
    this.postId = postId;
    this.breed = breed;
    this.sex = sex;
    this.age =age;
    this.ownerType =ownerType;
    this.address = address;
    this.isAdopted = isAdopted;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;

    String[] str = imgs.split(" ## ");
    for (String x : str) {
      this.img.add(x);
    }
  }
}

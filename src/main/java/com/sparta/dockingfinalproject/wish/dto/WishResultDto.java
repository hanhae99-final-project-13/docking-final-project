package com.sparta.dockingfinalproject.wish.dto;

import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Sex;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WishResultDto {

  private Long wishId;
  private Long postId;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String breed;
  private Sex sex;
  private int age;
  private String ownerType;
  private String address;
  private String img;
  private IsAdopted isAdopted;

  public WishResultDto(Long wishId, Long postId, LocalDateTime createdAt, LocalDateTime modifiedAt,
      String breed, Sex sex, int age, String ownerType, String address, String img,
      IsAdopted isAdopted) {
    this.wishId = wishId;
    this.postId = postId;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.breed = breed;
    this.sex = sex;
    this.age = age;
    this.ownerType = ownerType;
    this.address = address;
    this.img = img;
    this.isAdopted = isAdopted;
  }
}
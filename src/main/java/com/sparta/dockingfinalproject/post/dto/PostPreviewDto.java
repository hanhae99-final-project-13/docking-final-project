package com.sparta.dockingfinalproject.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostPreviewDto {

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

  @QueryProjection
  public PostPreviewDto(Long postId, LocalDateTime createdAt, LocalDateTime modifiedAt, String breed, Sex sex,
      int age, String ownerType, String address, String imgs, IsAdopted isAdopted) {
    this.postId = postId;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.breed = breed;
    this.sex = sex;
    this.age = age;
    this.ownerType = ownerType;
    this.address = address;
    this.isAdopted = isAdopted;

    String[] str = imgs.split(" ## ");
    this.img = str[0];
  }

  public static PostPreviewDto of(Post post) {
    String[] str = post.getPet().getImg().split(" ## ");
    return new PostPreviewDto(post.getPostId(), post.getPet().getCreatedAt(), post.getPet().getModifiedAt(), post.getPet().getBreed(), post.getPet().getSex(),
        post.getPet().getAge(), post.getPet().getOwnerType(), post.getPet().getAddress(), str[0], post.getPet().getIsAdopted());
  }
}
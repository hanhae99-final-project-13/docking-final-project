package com.sparta.dockingfinalproject.post.dto;

import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
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
  private String isAdopted;

  public static PostPreviewDto of(Post post) {
    Pet pet = post.getPet();
    return PostPreviewDto.builder()
        .postId(post.getPostId())
        .createdAt(post.getCreatedAt())
        .modifiedAt(post.getModifiedAt())
        .postId(post.getPostId())
        .breed(pet.getBreed())
        .sex(pet.getSex())
        .age(pet.getAge())
        .ownerType(pet.getOwnerType())
        .address(pet.getAddress())
        .img(pet.getImg())
        .isAdopted(pet.getIsAdopted())
        .build();
  }
}
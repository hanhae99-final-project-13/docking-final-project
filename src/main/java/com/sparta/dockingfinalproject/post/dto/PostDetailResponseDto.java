package com.sparta.dockingfinalproject.post.dto;

import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetSex;
import com.sparta.dockingfinalproject.post.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailResponseDto {
  private Long postId;
  private String breed;
  private PetSex sex;
  private int age;
  private double weight;
  private String lostLocation;
  private String ownerType;
  private String phone;
  private String tag;
  private String url;
  private String img;
  private String isAdopted;
  private boolean heart;

  public static PostDetailResponseDto getPostDetailResponseDto(Post post, boolean heart) {
    Pet pet = post.getPet();
    return PostDetailResponseDto.builder()
            .postId(post.getPostId())
            .breed(pet.getBreed())
            .sex(pet.getSex())
            .weight(pet.getWeight())
            .lostLocation(pet.getLostLocation())
            .ownerType(pet.getOwnerType())
            .phone(pet.getPhone())
            .tag(pet.getTag())
            .url(pet.getUrl())
            .img(pet.getImg())
            .isAdopted(pet.getIsAdopted())
            .heart(heart)
            .build();
  }

  public static PostDetailResponseDto getPostDetailResponseDto(Post post) {
    Pet pet = post.getPet();
    return PostDetailResponseDto.builder()
        .postId(post.getPostId())
        .breed(pet.getBreed())
        .sex(pet.getSex())
        .weight(pet.getWeight())
        .lostLocation(pet.getOwnerType())
        .ownerType(pet.getOwnerType())
        .phone(pet.getPhone())
        .tag(pet.getTag())
        .url(pet.getUrl())
        .img(pet.getImg())
        .isAdopted(pet.getIsAdopted())
        .build();
  }
}

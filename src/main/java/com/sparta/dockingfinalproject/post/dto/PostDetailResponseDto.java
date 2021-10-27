package com.sparta.dockingfinalproject.post.dto;

import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetSex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailResponseDto {
  private String petName;
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
  private boolean isAdopted;
  private boolean heart;

  public static PostDetailResponseDto getPostDetailResponseDto(Pet pet, boolean heart) {
    return PostDetailResponseDto.builder()
            .petName(pet.getPetName())
            .breed(pet.getBreed())
            .sex(pet.getSex())
            .weight(pet.getWeight())
            .lostLocation(pet.getOwnerType())
            .ownerType(pet.getOwnerType())
            .phone(pet.getPhone())
            .tag(pet.getTag())
            .url(pet.getUrl())
            .img(pet.getImg())
            .isAdopted(pet.isAdopted())
            .heart(heart)
            .build();
  }
}

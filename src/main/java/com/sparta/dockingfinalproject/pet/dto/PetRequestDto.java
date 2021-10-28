package com.sparta.dockingfinalproject.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequestDto {
  private String breed;
  private String sex;
  private int age;
  private double weight;
  private String lostLocation;
  private String ownerType;
  private String address;
  private String phone;
  private String tag;
  private String url;
  private String img;
  private String extra;
  private boolean isAdopted;
}

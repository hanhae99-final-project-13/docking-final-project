package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.pet.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FosterFormRequestDto {

  private String name;

  private Long fosterAge;

  private Sex gender;

  private String phone;

  private String job;

  private String fosterAddress;

  private String currentPet;

  private String experience;

  private String reason;

  private String allergy;

  private String family;

  private String timeTogether;

  private String anxiety;

  private String bark;

  private String roomUrl;

}
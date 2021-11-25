package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.common.Enum;
import com.sparta.dockingfinalproject.exception.ErrorMessage;
import com.sparta.dockingfinalproject.pet.Sex;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FosterFormRequestDto {

  @NotBlank(message = ErrorMessage.NAME_REQUIRED)
  private String name;

  @Positive(message = ErrorMessage.CHECK_AGE)
  @NotNull(message = ErrorMessage.FOSTER_AGE_REQUIRED)
  private Long fosterAge;

  @Enum(enumClass = Sex.class, message = ErrorMessage.GENDER_REQUIRED)
  private String gender;

  @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$", message = ErrorMessage.CHECK_PHONE_LENGTH)
  @Length(min = 9, max = 11, message = ErrorMessage.CHECK_PHONE_LENGTH)
  @NotNull(message = ErrorMessage.PHONE_REQUIRED)
  private String phone;

  @NotBlank(message = ErrorMessage.JOB_REQUIRED)
  private String job;

  @NotBlank(message = ErrorMessage.ADDRESS_REQUIRED)
  private String fosterAddress;

  @NotBlank(message = ErrorMessage.CURRENT_PET_REQUIRED)
  private String currentPet;

  @NotBlank(message = ErrorMessage.EXPERIENCE_REQUIRED)
  private String experience;

  @NotBlank(message = ErrorMessage.REASON_REQUIRED)
  private String reason;

  @NotBlank(message = ErrorMessage.ALLERGY_REQUIRED)
  private String allergy;

  @NotBlank(message = ErrorMessage.FAMILY_REQUIRED)
  private String family;

  @NotBlank(message = ErrorMessage.TIME_TOGETHER_REQUIRED)
  private String timeTogether;

  @NotBlank(message = ErrorMessage.ANXIETY_REQUIRED)
  private String anxiety;

  @NotBlank(message = ErrorMessage.BARK_REQUIRED)
  private String bark;

  @NotBlank(message = ErrorMessage.ROOM_URL_REQUIRED)
  private String roomUrl;

}
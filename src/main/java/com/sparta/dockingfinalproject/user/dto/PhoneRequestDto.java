package com.sparta.dockingfinalproject.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRequestDto {

  private String phoneNumber;
  private String randomNumber;
  private boolean authCheck;


}

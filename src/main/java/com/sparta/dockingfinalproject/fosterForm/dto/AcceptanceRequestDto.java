package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.common.Enum;
import com.sparta.dockingfinalproject.exception.ErrorMessage;
import com.sparta.dockingfinalproject.fosterForm.Acceptance;
import lombok.Getter;

@Getter
public class AcceptanceRequestDto {

  @Enum(enumClass = Acceptance.class, message = ErrorMessage.ACCEPTANCE_REQUIRED)
  private String acceptance;

}

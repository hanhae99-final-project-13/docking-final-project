package com.sparta.dockingfinalproject.fosterForm.dto;

import com.sparta.dockingfinalproject.common.annotation.EnumTypeValid;
import com.sparta.dockingfinalproject.exception.ErrorMessage;
import com.sparta.dockingfinalproject.fosterForm.model.Acceptance;
import lombok.Getter;

@Getter
public class AcceptanceRequestDto {

  @EnumTypeValid(enumClass = Acceptance.class, message = ErrorMessage.ACCEPTANCE_REQUIRED)
  private String acceptance;

}

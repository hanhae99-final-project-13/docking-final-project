package com.sparta.dockingfinalproject.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestApiException {
  private String status;
  private String errorMessage;
  private HttpStatus httpStatus;
}

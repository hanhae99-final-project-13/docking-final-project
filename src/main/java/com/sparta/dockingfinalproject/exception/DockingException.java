package com.sparta.dockingfinalproject.exception;

import lombok.Getter;

@Getter
public class DockingException extends RuntimeException {

  private final ErrorCode errorCode;

  public DockingException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}

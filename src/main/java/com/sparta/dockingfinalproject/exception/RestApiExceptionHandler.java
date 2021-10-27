package com.sparta.dockingfinalproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

  @ExceptionHandler(value = {DockingException.class})
  public ResponseEntity<Object> handleApiRequestException(DockingException e) {
    RestApiException restApiException = new RestApiException();
    restApiException.setStatus("fail");
    restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
    restApiException.setErrorMessage(e.getErrorCode().getMessage());

    return new ResponseEntity<>(
        restApiException,
        HttpStatus.BAD_REQUEST
    );
  }

}

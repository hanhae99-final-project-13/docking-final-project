package com.sparta.dockingfinalproject.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getAllErrors()
        .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }

}

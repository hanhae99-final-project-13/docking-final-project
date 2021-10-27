package com.sparta.dockingfinalproject.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  EMAIL_DUPLICATE(BAD_REQUEST, "중복된 email이 존재합니다."),
  USER_NOT_FOUND(BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),
  LOGIN_TOKEN_EXPIRE(BAD_REQUEST, "로그인이 만료되었습니다. 재로그인 하세요."),

  POST_NOT_FOUND(BAD_REQUEST, "해당 게시글을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}

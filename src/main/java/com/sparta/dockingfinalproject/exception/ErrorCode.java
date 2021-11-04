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
  POST_NOT_FOUND(BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
  ALARM_NOT_FOUND(BAD_REQUEST, "해당 알람 내용을 찾을 수 없습니다."),
  NO_AUTHORIZATION(BAD_REQUEST, "권한이 없습니다."),
  NO_DIFFERENCE(BAD_REQUEST, "변경된 사항이 없습니다."),
  PASSWORD_MISS_MATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
  USERNAME_MISS_MATCH(BAD_REQUEST, "아이디가 일치하지 않습니다"),
  USERNAME_DUPLICATE(BAD_REQUEST, "중복된 username이 존재합니다."),
  NICKNAME_DUPLICATE(BAD_REQUEST, "중복된 nickname이 존재합니다."),


  PET_NOT_FOUND(BAD_REQUEST, "해당 유기견을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}

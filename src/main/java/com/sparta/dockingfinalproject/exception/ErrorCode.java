package com.sparta.dockingfinalproject.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  USERNAME_NOT_FOUND(BAD_REQUEST, "아이디를 중복확인을 먼저 해주세요."),
  USERNAME_MISS_MATCH(BAD_REQUEST, "아이디가 일치하지 않습니다."),
  USERNAME_DUPLICATE(BAD_REQUEST, "중복된 아이디가 존재합니다."),
  NICKNAME_DUPLICATE(BAD_REQUEST, "중복된 닉네임이 존재합니다."),
  NICKNAME_NOT_FOUND(BAD_REQUEST, "닉네임 중복확인을 먼저 해주세요."),
  EMAIL_DUPLICATE(BAD_REQUEST, "중복된 이메일이 존재합니다."),
  IMAGE_NOT_FOUND(BAD_REQUEST, "이미지가 없습니다."),
  CODE_NOT_FOUND(BAD_REQUEST, "카카오 사용자를 찾을 수 없습니다."),
  PASSWORD_MISS_MATCH(BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

  NUMBER_MISS_MATCH(BAD_REQUEST, "인증번호가 맞지 않습니다."),
  NUMBER_NOT_FOUND(BAD_REQUEST, "핸드폰번호를 입력해주세요."),

  USER_NOT_FOUND(BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),
  LOGIN_REQUIRED(BAD_REQUEST, "로그인이 필요합니다."),
  LOGIN_TOKEN_EXPIRE(BAD_REQUEST, "로그인이 만료되었습니다. 재로그인 하세요."),
  NO_AUTHORIZATION(BAD_REQUEST, "권한이 없습니다."),
  EMAIL_NOT_FOUND(BAD_REQUEST, "이메일 인증을 먼저 진행해주셔야 합니다. 이메일 함을 확인해주세요"),
  NO_PHONE_AUTHENTICATION(BAD_REQUEST, "핸드폰 인증을 완료해주세요"),

  NO_DIFFERENCE(BAD_REQUEST, "변경된 사항이 없습니다."),
  NO_AVAILABILITY(BAD_REQUEST, "신청 가능 대상이 아닙니다."),
  REQUEST_DUPLICATE(BAD_REQUEST, "이미 신청 완료 하였습니다."),

  POST_NOT_FOUND(BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
  COMMENT_NOT_FOUND(BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
  FOSTERFORM_NOT_FOUND(BAD_REQUEST, "해당 입양신청서를 찾을 수 없습니다."),
  ALARM_NOT_FOUND(BAD_REQUEST, "해당 알람 내용을 찾을 수 없습니다."),
  PET_NOT_FOUND(BAD_REQUEST, "해당 유기견을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}

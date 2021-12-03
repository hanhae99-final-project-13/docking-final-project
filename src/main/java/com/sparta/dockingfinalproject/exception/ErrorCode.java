package com.sparta.dockingfinalproject.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  USERNAME_NOT_FOUND(BAD_REQUEST, "아이디를 찾을 수 없습니다."),
  USERNAME_EMPTY(BAD_REQUEST, "아이디를 입력해주세요."),
  USERNAME_MISS_MATCH(BAD_REQUEST, "아이디가 일치하지 않습니다."),
  USERNAME_DUPLICATE(BAD_REQUEST, "중복된 아이디가 존재합니다."),

  NICKNAME_DUPLICATE(BAD_REQUEST, "중복된 닉네임이 존재합니다."),
  NICKNAME_NOT_FOUND(BAD_REQUEST, "닉네임 중복확인을 먼저 해주세요."),
  NICKNAME_EMPTY(BAD_REQUEST, "닉네임을 입력해주세요"),

  EMAIL_NO_AVAILABILITY(BAD_REQUEST, "이메일 형식에 맞게 입력해주세요"),
  EMAIL_DUPLICATE(BAD_REQUEST, "중복된 이메일이 존재합니다."),
  IMAGE_NOT_FOUND(BAD_REQUEST, "이미지가 없습니다."),
  CODE_NOT_FOUND(BAD_REQUEST, "카카오 사용자를 찾을 수 없습니다."),

  PASSWORD_EMPTY(BAD_REQUEST, "비밀번호를 입력해주세요."),
  PASSWORD_MISS_MATCH(BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
  PASSWORD_NOT_FOUND(BAD_REQUEST, "비밀번호를 다시 입력해주세요"),
  PASSWORD_NOT_AVALABILITY(BAD_REQUEST, "비밀번호는 숫자와 영문자를 1개 이상 포함, 8자 이상이여야 합니다."),

  RANDOMNUMVER_EMPTY(BAD_REQUEST, "인증 번호를 입력해주세요."),
  NUMBER_MISS_MATCH(BAD_REQUEST, "인증번호가 맞지 않습니다."),
  NUMBER_REQUIRED(BAD_REQUEST, "핸드폰번호를 입력해주세요."),

  USER_NOT_FOUND(BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),
  LOGIN_REQUIRED(BAD_REQUEST, "로그인이 필요합니다."),
  LOGIN_TOKEN_EXPIRE(BAD_REQUEST, "로그인이 만료되었습니다. 재로그인 하세요."),
  NO_AUTHORIZATION(BAD_REQUEST, "권한이 없습니다."),
  EMAIL_NOT_FOUND(BAD_REQUEST, "이메일을 찾을 수 없습니다."),
  NO_PHONE_AUTHENTICATION(BAD_REQUEST, "핸드폰 인증을 완료해주세요"),

  NO_DIFFERENCE(BAD_REQUEST, "변경된 사항이 없습니다."),
  NO_AVAILABILITY(BAD_REQUEST, "신청 가능 대상이 아닙니다."),
  REQUEST_DUPLICATE(BAD_REQUEST, "이미 신청 완료 하였습니다."),
  NOT_AVAILABLE_FOR_MINE(BAD_REQUEST, "본인 글에 신청할 수 없습니다."),

  POST_NOT_FOUND(BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
  COMMENT_NOT_FOUND(BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
  FOSTERFORM_NOT_FOUND(BAD_REQUEST, "해당 입양신청서를 찾을 수 없습니다."),
  ALARM_NOT_FOUND(BAD_REQUEST, "해당 알람 내용을 찾을 수 없습니다."),
  PET_NOT_FOUND(BAD_REQUEST, "해당 유기견을 찾을 수 없습니다."),

  EDUCATION_NOT_FOUND(BAD_REQUEST, "이수된 교육정보가 없습니다."),
  BASIC_EDUCATION_REQUIRED(BAD_REQUEST, "필수지식 이수를 완료해주세요."),

  CLASSNUMBER_NOT_FOUND(BAD_REQUEST, "해당 페이지에 대한 정보가 없습니다.");


  private final HttpStatus httpStatus;
  private final String message;
}
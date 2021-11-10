package com.sparta.dockingfinalproject.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoUserController {


  private final KakaoUserService kakaoUserService;

  public KakaoUserController(KakaoUserService kakaoUserService ) {

	this.kakaoUserService = kakaoUserService;
  }

  //카카오 인가 코드 받기
  @GetMapping("/oauth/callback/kakao")
  public Map<String, Object> kakaoLogin(@RequestParam String code) throws JsonProcessingException {

    return kakaoUserService .kakaoLogin(code);

  }
}
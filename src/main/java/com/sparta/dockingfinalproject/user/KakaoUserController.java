package com.sparta.dockingfinalproject.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoUserController {

  private final KakaoUserService kakaoUserService;

  public KakaoUserController(KakaoUserService kakaoUserService) {

    this.kakaoUserService = kakaoUserService;
  }

  //카카오 인가 코드 받기
  @GetMapping("/oauth/callback/kakao")
  public Map<String, Object> kakaoLogin(@RequestParam String code) throws JsonProcessingException {

    return kakaoUserService.kakaoLogin(code);

  }
}
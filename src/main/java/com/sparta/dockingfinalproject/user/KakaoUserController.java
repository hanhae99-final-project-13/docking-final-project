package com.sparta.dockingfinalproject.user;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class KakaoUserController {


    private final KakaoUserService kakaoUserService;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoUserController(KakaoUserService kakaoUserService, JwtTokenProvider jwtTokenProvider) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoUserService = kakaoUserService;
    }

    //카카오 인가 코드 받기
    @GetMapping("/oauth/callback/kakao")
    public Map<String, Object> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        List<Map<String, Object>> applyList = new ArrayList<>();
        User user = kakaoUserService.kakaoLogin(code);
//        String img = "이미지 url";
        Map<String, Object> result2 = new HashMap<>();
        result2.put("postId", "postId");
        result2.put("applyState", "complete");
        applyList.add(result2);




        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtTokenProvider.createToken(user.getEmail(),user.getEmail()));
        data.put("nickname", user.getNickname());
        data.put("email", user.getEmail());
        data.put("classCount",0 );
        data.put("alarmCount",0 );
        data.put("img", user.getUserImgUrl());
        data.put("applyList", applyList);


        result.put("data",data);

        return result;

    }



}
package com.sparta.dockingfinalproject.user;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
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
        Map<String, Object> data = new HashMap<>();

        if(code != null) {

            Map<String, Object> apply = new HashMap<>();

            data.put("nickname", user.getNickname());
            data.put("email", user.getEmail());
            data.put("userImgUrl", user.getUserImgUrl());
            data.put("classCount", 3);
            data.put("alarmCount", 3);
            data.put("applyList", applyList);
            data.put("token", jwtTokenProvider.createToken(user.getEmail(),user.getEmail()));
            apply.put("applyState", "디폴트");
            apply.put("postId", "디폴트");
            applyList.add(apply);
        } else{
            throw new DockingException(ErrorCode.CODE_NOT_FOUND);
        }
        System.out.println("카카오 로그인 완료");
        return SuccessResult.success(data);

//        String img = "이미지 url";
//        Map<String, Object> result2 = new HashMap<>();
//        result2.put("postId", "postId");
//        result2.put("applyState", "complete");
//        applyList.add(result2);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("status", "success");
//        Map<String, Object> data = new HashMap<>();
//        data.put("token", jwtTokenProvider.createToken(user.getEmail(),user.getEmail()));
//        data.put("nickname", user.getNickname());
//        data.put("email", user.getEmail());
//        data.put("classCount",0 );
//        data.put("alarmCount",0 );
//        data.put("userImgUrl", user.getUserImgUrl());
//        data.put("applyList", applyList);
//
//
//        result.put("data",data);
//
//        return result;

    }



}
package com.sparta.dockingfinalproject.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.ResponseDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUserService kakaoUserService;

    public UserController(UserService userService, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, KakaoUserService kakaoUserService)
    {
        this.userService = userService;
        this.userRepository  = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoUserService = kakaoUserService;

    }

    //회원가입 요청
    @PostMapping("/signup")
    public Map<String, Object> registerUser (@RequestBody SignupRequestDto requestDto){

        Map<String, Object>result = new HashMap<>();
        result.put("status", "success");
        Map<String, Object> message = new HashMap<>();
        message.put("msg", "회원가입 완료");
        result.put("data", message);
        userService.registerUser(requestDto);

        return result;
    }

    //로그인 요청
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserRequestDto requestDto, ResponseDto responseDto) {

        User user = userService.login(requestDto);
        Map<String, Object> result= new HashMap<>();

        result.put("status", "success");
        responseDto.setNickname(user.getNickname());
        responseDto.setEmail(user.getEmail());
        responseDto.setToken(jwtTokenProvider.createToken(requestDto.getUsername()));
        result.put ("data", responseDto);

        return result;

    }

    //로그인체크
    @GetMapping("/login/check")
    public Map<String, Object> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails, ResponseDto responseDto){

        if(userDetails == null){
            throw new IllegalArgumentException("로그인이 만료되었습니다. 다시 로그인 해주세요");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("status","success");
        Map<String, Object> data = new HashMap<>();
        data.put("nickname",userDetails.getUser().getNickname());
        data.put("email",userDetails.getUser().getEmail());
        data.put("classCount",0 );
        data.put("alarmCount",0 );
        result.put("data", data);

        return result;
    }





    //카카오 인가 코드 받기
    @GetMapping("/oauth/callback/kakao")
    public Map<String, Object> kakaoLogin(@RequestParam String code) throws JsonProcessingException {

       User user = kakaoUserService.kakaoLogin(code);


       Map<String, Object> result = new HashMap<>();
       result.put("status", "success");
       Map<String, Object> data = new HashMap<>();
       data.put("token", jwtTokenProvider.createToken(user.getEmail()));
       data.put("nickname", user.getNickname());
       data.put("email", user.getEmail());
       data.put("classCount",0 );
       data.put("alarmCount",0 );

       result.put("data",data);

       return result;

    }


    //아이디 중복 확인
    @GetMapping("/signup/checkid")
    public Map<String, Object> idDoubleCheck(@RequestParam String username) {

        Optional<User> found = userRepository.findByUsername(username);

        Map<String, Object>result = new HashMap<>();
        result.put("status", "success");
        Map<String, Object> message = new HashMap<>();

        if(found.isPresent()){
            message.put("msg",false);

        } else {
            message.put("msg",true);
        }
        result.put("data", message);
        return result;
    }

    //닉네임 중복체크
    @GetMapping("/signup/checknickname")
    public Map<String, Object> nicknameDoubleCheck(@RequestParam String nickname) {
        Optional<User> found = userRepository.findByNickname(nickname);

        Map<String, Object>result = new HashMap<>();
        result.put("status", "success");
        Map<String, Object> message = new HashMap<>();

        if(found.isPresent()){
            message.put("msg",false);

        } else {
            message.put("msg",true);
        }
        result.put("data", message);
        return result;
    }

}

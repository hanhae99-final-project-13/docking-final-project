package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.ResponseDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, UserRepository userRepository, JwtTokenProvider jwtTokenProvider)
    {
        this.userService = userService;
        this.userRepository  = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;

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
        result.put("data", data);


        return result;
    }
}

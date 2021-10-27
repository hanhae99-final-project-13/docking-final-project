package com.sparta.dockingfinalproject.user;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public Map<String, Object> registerUser (@RequestBody SignupRequestDto requestDto){

        Map<String, Object>result = new HashMap<>();
        result.put("status", "success");
        Map<String, Object> message = new HashMap<>();
        message.put("msg", "회원가입 완료");
        userService.registerUser(requestDto);

        return result;

    }
}

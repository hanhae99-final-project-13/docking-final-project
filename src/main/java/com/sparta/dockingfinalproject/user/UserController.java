package com.sparta.dockingfinalproject.user;



import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import com.sparta.dockingfinalproject.user.dto.ResponseDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.mail.MailSendService;
import com.sparta.dockingfinalproject.user.phoneMessage.PhoneService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailSendService mailSendService;

    public UserController(UserService userService, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, MailSendService mailSendService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailSendService = mailSendService;
   }

    //회원가입 요청
    @PostMapping("/signup")

    //bindingResult가 뭘까?????????????
    public Map<String, Object> registerUser(@RequestBody SignupRequestDto requestDto) throws Exception {

        Map<String, Object> result = new HashMap<>();
        System.out.println(requestDto.getUsername() + "회원가입 요청");


        try {

            Map<String,String>message = new HashMap<>();
            message.put("msg", "회원가입을 축하드립니다");

            //임의의 authKey생성, 이메일 발송

            String authKey = mailSendService.sendSimpleMessage(requestDto.getEmail());
            userService.registerUser(requestDto, authKey);

            return SuccessResult.success(message);

        } catch (DockingException e) {
            result.put("status", "fail");
            return result;
        }


    }



    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////


    //로그인 요청
    @PostMapping("/user/login")
    public Map<String, Object> login(@RequestBody SignupRequestDto requestDto, ResponseDto responseDto) throws DockingException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> result2 = new HashMap<>();
        List<Map<String, Object>> applyList = new ArrayList<>();
        System.out.println(requestDto.getUsername() + "로그인 요청");

        try {

            result2.put("postId", "postId");
            result2.put("applyState", "complete");
            applyList.add(result2);
            User user = userService.login(requestDto);
            Optional<User> found = userRepository.findByUsername(requestDto.getUsername());


            result.put("status", "success");
            responseDto.setNickname(user.getNickname());
            responseDto.setEmail(user.getEmail());
            responseDto.setToken(jwtTokenProvider.createToken(requestDto.getUsername(),requestDto.getUsername()));
            //토큰에다가 username 1개의 정보를 집어 넣음
            //creatToken(requestDto.getEmail(), requestDto.getPassword(), requestDto.getUsername,,이런식으로 정보를 더 넣을 수 있다)
            responseDto.setUserImgUrl(user.getUserImgUrl());
            responseDto.setApplyList(applyList);



            result.put("data", responseDto);
//            result.put("data", applyList);

            return result;

        } catch (Exception e) {
            result.put("status", "fail");
            return result;
        }

    }


    //로그인체크
    @GetMapping("/user/check")
    public Map<String, Object> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) throws DockingException {


        List<Map<String, Object>> applyList = new ArrayList<>();
        Map<String, Object> result2 = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        try {
            result2.put("postId", "postId");
            result2.put("applyState", "complete");
            applyList.add(result2);

            if (userDetails == null) {
                throw new IllegalArgumentException("로그인이 만료되었습니다. 다시 로그인 해주세요");
            }

            result.put("status", "success");
            Map<String, Object> data = new HashMap<>();
            data.put("nickname", userDetails.getUser().getNickname());
            data.put("email", userDetails.getUser().getEmail());
            data.put("classCount", 0);
            data.put("alarmCount", 0);

            //추가부분
            data.put("userImgUrl", userDetails.getUser().getUserImgUrl());
            data.put("applyList", applyList);
            result.put("data", data);
            return result;


        } catch (Exception e) {

            result.put("status", "fail");
            return result;

        }
    }


    //아이디 중복 확인
    @GetMapping("/signup/checkid")
    public Map<String, Object> idDoubleCheck(@RequestParam String username) throws DockingException {
        Map<String, Object> result = new HashMap<>();
        Optional<User> found = userRepository.findByUsername(username);
        Map<String, Object> message = new HashMap<>();


        if(!found.isPresent()){
            result.put("status", "success");
            message.put("msg","중복 확인 완료");
            result.put("data", message);

            return result;
        } else {
            result.put("status", "fail");
            message.put("msg","중복 아이디입니다");
            result.put("data", message);
            return result;
        }

    }


    //닉네임 중복체크
    @GetMapping("/signup/checknickname")
    public Map<String, Object> nicknameDoubleCheck(@RequestParam String nickname) throws DockingException {
        Map<String, Object> result = new HashMap<>();
        Optional<User> found = userRepository.findByNickname(nickname);
        Map<String, Object> message = new HashMap<>();


        if(!found.isPresent()){
            result.put("status", "success");
            message.put("msg","중복 확인 완료");
            result.put("data", message);

            return result;
        } else {
            result.put("status", "fail");
            message.put("msg","중복 아이디입니다");
            result.put("data", message);
            return result;
        }

    }



}
package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UpdateRequestDto;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

  private final UserService userService;
  public UserController(UserService userService	) {

	this.userService = userService;

  }

  //회원가입 요청
  @PostMapping("/signup")

  //bindingResult가 뭘까?????????????
  public Map<String, Object> registerUser(@RequestBody SignupRequestDto requestDto)
	  throws Exception {
	return userService.registerUser(requestDto);
  }


  //로그인 요청
  @PostMapping("/user/login")
  public Map<String, Object> login(@RequestBody SignupRequestDto requestDto)
	  throws DockingException {
	return userService.login(requestDto);
  }

  //유저 수정

  @PatchMapping("/user")
  public Map<String, Object> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
	  @RequestBody UpdateRequestDto requestDto) {

	System.out.println("수정에 도착");

	return userService.updateUser(userDetails, requestDto);

  }


  //로그인체크
  @GetMapping("/user/check")
  public Map<String, Object> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails)
	  throws DockingException {

	return userService.loginCheck(userDetails);

  }


  //아이디 중복 확인
  @GetMapping("/signup/checkid")
  public Map<String, Object> idDoubleCheck(@RequestParam String username) throws DockingException {

	return userService.idDoubleCheck(username);

  }


  //닉네임 중복체크
  @GetMapping("/signup/checknickname")
  public Map<String, Object> nicknameDoubleCheck(@RequestParam String nickname)
	  throws DockingException {

	return userService.nicknameDoubleCheck(nickname);

  }
}
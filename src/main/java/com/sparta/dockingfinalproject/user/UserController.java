package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.security.jwt.TokenRequestDto;
import com.sparta.dockingfinalproject.user.dto.request.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.request.UpdateRequestDto;
import com.sparta.dockingfinalproject.user.dto.request.UserInquriryRequestDto;
import com.sparta.dockingfinalproject.user.dto.request.UserRequestDto;
import com.sparta.dockingfinalproject.user.mail.MailSendService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
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
  private final MailSendService mailSendService;

  public UserController(UserService userService, MailSendService mailSendService) {

	this.userService = userService;
	this.mailSendService = mailSendService;

  }

  @PostMapping("/signup")
  public Map<String, Object> registerUser(@RequestBody SignupRequestDto requestDto) {
	return userService.registerUser(requestDto);
  }


  @PostMapping("/user/login")
  public Map<String, Object> login(@RequestBody UserRequestDto requestDto)
	  throws DockingException {
	return userService.login(requestDto);
  }

  @PatchMapping("/user")
  public Map<String, Object> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
	  @RequestBody UpdateRequestDto requestDto) {
	return userService.updateUser(userDetails, requestDto);
  }


  @GetMapping("/user/check")
  public Map<String, Object> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails)
	  throws DockingException {
	return userService.loginCheck(userDetails);
  }


  @GetMapping("/signup/checkid")
  public Map<String, Object> idDoubleCheck(@RequestParam String username) throws DockingException {
	return userService.idDoubleCheck(username);
  }


  @GetMapping("/signup/checknickname")
  public Map<String, Object> nicknameDoubleCheck(@RequestParam String nickname)
	  throws DockingException {
	return userService.nicknameDoubleCheck(nickname);
  }

  @PostMapping("/idInquiry")
  public Map<String, Object> findUserId(
	  @RequestBody UserInquriryRequestDto userInquriryRequestDto) {
	return userService.findUserId(userInquriryRequestDto);
  }

  @PostMapping("/pwInquiry")
  public Map<String, Object> findUserPw(
	  @RequestBody UserInquriryRequestDto userInquriryRequestDto) {
	String tempPw = mailSendService.sendSimpleMessage(userInquriryRequestDto.getEmail());
	Map<String, Object> result = userService.findUserPw(userInquriryRequestDto, tempPw);
	return result;
  }

  @PostMapping("/reissue")
  public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
	return ResponseEntity.ok(userService.reissue(tokenRequestDto));
  }

}
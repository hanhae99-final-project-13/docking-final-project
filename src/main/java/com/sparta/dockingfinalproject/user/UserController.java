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
  public ResponseEntity<Map<String, Object>> registerUser(@RequestBody SignupRequestDto requestDto) {
    return ResponseEntity.ok().body(userService.registerUser(requestDto));
  }


  @PostMapping("/user/login")
  public ResponseEntity<Map<String, Object>> login(@RequestBody UserRequestDto requestDto)
      throws DockingException {
    return ResponseEntity.ok().body(userService.login(requestDto));
  }

  @PatchMapping("/user")
  public ResponseEntity<Map<String, Object>> updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody UpdateRequestDto requestDto) {
    return ResponseEntity.ok().body(userService.updateUser(userDetails, requestDto));
  }


  @GetMapping("/user/check")
  public ResponseEntity<Map<String, Object>> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails)
      throws DockingException {
    return ResponseEntity.ok().body(userService.loginCheck(userDetails));
  }


  @GetMapping("/signup/checkid")
  public ResponseEntity<Map<String, Object>> idDoubleCheck(@RequestParam String username) throws DockingException {
    return ResponseEntity.ok().body(userService.idDoubleCheck(username));
  }


  @GetMapping("/signup/checknickname")
  public ResponseEntity<Map<String, Object>> nicknameDoubleCheck(@RequestParam String nickname)
      throws DockingException {
    return ResponseEntity.ok().body(userService.nicknameDoubleCheck(nickname));
  }

  @PostMapping("/idInquiry")
  public ResponseEntity<Map<String, Object>> findUserId(
      @RequestBody UserInquriryRequestDto userInquriryRequestDto) {
    return ResponseEntity.ok().body(userService.findUserId(userInquriryRequestDto));
  }

  @PostMapping("/pwInquiry")
  public ResponseEntity<Map<String, Object>> findUserPw(
      @RequestBody UserInquriryRequestDto userInquriryRequestDto) {
    String tempPw = mailSendService.sendSimpleMessage(userInquriryRequestDto.getEmail());
    return ResponseEntity.ok().body(userService.findUserPw(userInquriryRequestDto, tempPw));
  }

  @PostMapping("/reissue")
  public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
    return ResponseEntity.ok(userService.reissue(tokenRequestDto));
  }

}
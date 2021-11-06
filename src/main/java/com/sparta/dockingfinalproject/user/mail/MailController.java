package com.sparta.dockingfinalproject.user.mail;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.user.UserService;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

  private final MailSendService mailSendService;
  private final UserService userService;

  public MailController(MailSendService mailSendService, UserService userService){
	this.mailSendService = mailSendService;
	this.userService = userService;
  }

  @GetMapping("/emailCheck")
  public Map<String, Object>mailCheck(@RequestParam String email) throws Exception{
	Map<String, Object> data = new HashMap<>();
	String authKey = mailSendService.sendSimpleMessage(email);
	data.put("msg", "이메일을 확인해주세요");

	return SuccessResult.success(data);


  }

  @GetMapping("/user/signUpConfirm")
  public ResponseEntity<Object> signupConfirm(@RequestParam String email, @RequestParam String authKey)
	throws Exception {
	System.out.println(email);
	System.out.println(authKey);
	userService.singUpConfirm(email,authKey);

	URI redirectUri = new URI("http://localhost:8080");
	HttpHeaders httpHeaders = new HttpHeaders();
	httpHeaders.setLocation(redirectUri);
	return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

  }
}

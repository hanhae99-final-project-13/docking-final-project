package com.sparta.dockingfinalproject.user.mail;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
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
	if(email .isEmpty()) {
	throw new DockingException(ErrorCode.EMAIL_NOT_FOUND);
	//에러 코드 수정해야함
	}
	  String authKey = mailSendService.sendSimpleMessage(email);
	  data.put("msg", "이메일을 확인해주세요");

	return SuccessResult.success(data);


  }
}

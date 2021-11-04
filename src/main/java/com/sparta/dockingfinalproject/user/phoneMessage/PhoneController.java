package com.sparta.dockingfinalproject.user.phoneMessage;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import com.sparta.dockingfinalproject.user.UserService;
import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhoneController {


  private final PhoneService phoneService;
  private final UserService userService;
  private final UserRepository userRepository;

  public PhoneController(PhoneService phoneService, UserService userService, UserRepository userRepository){
	this.phoneService = phoneService;
	this.userService = userService;
	this.userRepository= userRepository;
  }


  @PostMapping("/checkNumber")
  public void sendMessage(@RequestBody PhoneRequestDto requestDto){
//	Map<String, Object> result = new HashMap<>();

	System.out.println("타고있니");
	String randomNumber = phoneService.sendMessage(requestDto);

  }


@PostMapping("/phoneConfirm")
  public Map<String, Object> phoneConfirm(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody PhoneRequestDto requestDto){

	userService.phoneConfirm(userDetails, requestDto);

  	Map<String, Object> data = new HashMap<>();
  	data.put("msg", "인증번호가 확인되었습니다");


  return SuccessResult.success(data);
}



}

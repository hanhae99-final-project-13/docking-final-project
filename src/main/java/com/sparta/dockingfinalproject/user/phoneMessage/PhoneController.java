package com.sparta.dockingfinalproject.user.phoneMessage;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import com.sparta.dockingfinalproject.user.UserService;
import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
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


  @PostMapping("/sendNumber")
  public Map<String, Object> sendMessage(@RequestBody SignupRequestDto requestDto){
	Map<String, Object> data = new HashMap<>();

	if(requestDto != null){

	phoneService.sendMessage(requestDto);
	data.put("msg", "인증 번호 발송 완료");} else {
	  throw new DockingException(ErrorCode.NUMBER_NOT_FOUND);

	}

	return SuccessResult.success(data);

  }


@PostMapping("/phoneConfirm")
  public Map<String, Object> phoneConfirm(@RequestBody SignupRequestDto singupRequestDto){
  Map<String, Object> data = new HashMap<>();

	phoneService.phoneConfirm(singupRequestDto);

  	data.put("msg", "인증번호가 확인되었습니다");


  return SuccessResult.success(data);
}



}

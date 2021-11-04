package com.sparta.dockingfinalproject.user.phoneMessage;

import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhoneController {

  private final PhoneService phoneService;

  public PhoneController(PhoneService phoneService){
	this.phoneService = phoneService;
  }


  @PostMapping("/confirmNumber")
  public void sendMessage(@RequestBody PhoneRequestDto requestDto){
//	Map<String, Object> result = new HashMap<>();

	System.out.println("타고있니");
	String randomNumber = "12345";

   	phoneService.sendMessage(requestDto, randomNumber);

  }


}

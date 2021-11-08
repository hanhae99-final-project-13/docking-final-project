package com.sparta.dockingfinalproject.user.phoneMessage;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.json.simple.JSONObject;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class PhoneService {

  private static Map<String,Integer> auth = new ConcurrentHashMap<>();



  private static int creatKey() {

	int randomNumber = (int)(Math.random() * (99999 - 10000 + 1)) + 10000;
	System.out.println(randomNumber);
	return randomNumber;

  }


  public int sendMessage (SignupRequestDto requestDto) {

	int randomNumber = creatKey();


	String api_key = "NCSSX04M9KLZMYOS";

	  String api_secret = "IEHPNYP3CS98ATRTT7CC11V6KJDVIZEP";

	  Message coolsms = new Message(api_key, api_secret);

	  // 4 params(to, from, type, text) are mandatory. must be filled
	  HashMap<String, String> params = new HashMap<>();
	  params.put("to",requestDto.getPhoneNumber());
	  params.put("from", "07079541724");
	  params.put("type", "SMS");
	System.out.println(randomNumber);
	  params.put("text", "인증번호  " + randomNumber + "  를 입력하세요. ");
	  params.put("app_version", "test app 1.2"); // application name and version

	  //	System.out.println(params);
	  try {
		JSONObject obj = (JSONObject) coolsms.send(params);

		System.out.println(obj.toString());
	  } catch (CoolsmsException e) {

		System.out.println(e.getMessage());

		System.out.println(e.getCode());
	  }
	  auth.put(requestDto.getUsername(), randomNumber);
	System.out.println(auth.get(requestDto.getUsername()));

	  return randomNumber;
	}


  //휴대폰 인증 확인

  public Map<String, Object> phoneConfirm(SignupRequestDto signupRequestDto) {
	Map<String, Object> data = new HashMap<>();


//	String randomNumber2 = Integer.toString(phoneService.sendMessage(signupRequestDto.getPhoneNumber()));
//	String randomNumber2 = "1234";
	System.out.println(auth.get(signupRequestDto.getUsername()));
	System.out.println(signupRequestDto.getRandomNumber());
	if (signupRequestDto.getRandomNumber().equals(auth.get(signupRequestDto.getUsername()))) {

	  data.put("msg", "인증번호가 일치합니다 ");

	  auth.remove(signupRequestDto.getUsername());
	  System.out.println(auth);
	  return SuccessResult.success(data);

	} else {
	  throw new DockingException(ErrorCode.NUMBER_MISS_MATCH);
	}

  }











}

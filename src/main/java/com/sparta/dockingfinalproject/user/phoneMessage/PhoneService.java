package com.sparta.dockingfinalproject.user.phoneMessage;

import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import java.util.HashMap;
import java.util.Random;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.json.simple.JSONObject;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class PhoneService {

  public static final int randomNumber = creatKey();


  private static int creatKey() {
	int randomNumber = (int)(Math.random() * (99999 - 10000 + 1)) + 10000;
	System.out.println(randomNumber);
	return randomNumber;

  }


  public int sendMessage ( PhoneRequestDto requestDto) {

	  String api_key = "NCSSX04M9KLZMYOS";

	  String api_secret = "IEHPNYP3CS98ATRTT7CC11V6KJDVIZEP";

	  Message coolsms = new Message(api_key, api_secret);

	  // 4 params(to, from, type, text) are mandatory. must be filled
	  HashMap<String, String> params = new HashMap<>();
	System.out.println(requestDto.getPhoneNumber());
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

	  return randomNumber;
	}

  }

package com.sparta.dockingfinalproject.user.phoneMessage;

import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import java.util.HashMap;

import java.util.Map;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;



@Service
public class PhoneService {

  public void sendMessage(PhoneRequestDto requestDto) {
	String api_key = "NCSSX04M9KLZMYOS";
	String api_secret = "IEHPNYP3CS98ATRTT7CC11V6KJDVIZEP";
	Message coolsms = new Message(api_key, api_secret);

	// 4 params(to, from, type, text) are mandatory. must be filled
	HashMap<String, String> params = new HashMap<>();
	params.put("to", requestDto.getPhoneNumber());
	params.put("from", "07079541724");
	params.put("type", "SMS");
	params.put("text", "이것은 지은이가 보낸 메세지!");
	params.put("app_version", "test app 1.2"); // application name and version

//	System.out.println(params);
	try {
	  JSONObject obj = (JSONObject) coolsms.send(params);
	  System.out.println(obj.toString());
	} catch (CoolsmsException e) {
	  System.out.println(e.getMessage());
	  System.out.println(e.getCode());
	}

  }

}

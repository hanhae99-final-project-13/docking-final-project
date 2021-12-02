package com.sparta.dockingfinalproject.user.phoneMessage;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.user.dto.request.SignupRequestDto;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service
public class PhoneService {

  private final RedisTemplate<String, Object> redisTemplate;

  public PhoneService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  private static int creatKey() {

    int randomNumber = (int) (Math.random() * (99999 - 10000 + 1)) + 10000;
    System.out.println(randomNumber);
    return randomNumber;

  }

  public int sendMessage(SignupRequestDto requestDto) {

    int randomNumber = creatKey();

    String api_key = "NCSSX04M9KLZMYOS";

    String api_secret = "IEHPNYP3CS98ATRTT7CC11V6KJDVIZEP";

    Message coolsms = new Message(api_key, api_secret);

    // 4 params(to, from, type, text) are mandatory. must be filled
    HashMap<String, String> params = new HashMap<>();
    params.put("to", requestDto.getPhoneNumber());
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
    authRedisSave(requestDto.getUsername(), randomNumber);

    return randomNumber;
  }

  private void authRedisSave(String username, int randomNumber) {
    final ValueOperations<String, Object> stringStringValueOperations = redisTemplate.opsForValue();
    stringStringValueOperations.set(username, randomNumber);
    redisTemplate.expire(username, 182, TimeUnit.SECONDS);
  }

  //휴대폰 인증 확인

  public Map<String, Object> phoneConfirm(SignupRequestDto signupRequestDto) {
    Map<String, Object> data = new HashMap<>();

//	String randomNumber2 = Integer.toString(phoneService.sendMessage(signupRequestDto.getPhoneNumber()));
//	String randomNumber2 = "1234";
    String username = signupRequestDto.getUsername();
    if (signupRequestDto.getRandomNumber().equals(authRedisFind(username))) {

      data.put("msg", "인증번호가 일치합니다 ");
      return SuccessResult.success(data);

    } else {
      throw new DockingException(ErrorCode.NUMBER_MISS_MATCH);
    }

  }

  private Integer authRedisFind(String username) {
    final ValueOperations<String, Object> stringStringValueOperations = redisTemplate.opsForValue();
    return (Integer) stringStringValueOperations.get(username);
  }
}

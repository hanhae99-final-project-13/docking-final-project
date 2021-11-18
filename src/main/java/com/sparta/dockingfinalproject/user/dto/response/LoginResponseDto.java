package com.sparta.dockingfinalproject.user.dto.response;

import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

  private Long userId;
  private String nickname;
  private String email;
  private String userImgUrl;
  private String phone;
  private List<Map<String, Object>> eduList;
  private int alarmCount;
  private TokenDto token;


  public static LoginResponseDto of(User user, TokenDto token, List<Map<String, Object>> eduList) {
    return LoginResponseDto.builder()
        .userId(user.getUserId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .userImgUrl(user.getUserImgUrl())
        .phone(user.getPhoneNumber())
        .eduList(eduList)
        .alarmCount(5)
        .token(token)
        .build();
  }
}

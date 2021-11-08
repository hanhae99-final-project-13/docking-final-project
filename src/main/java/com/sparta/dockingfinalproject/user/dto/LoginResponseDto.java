package com.sparta.dockingfinalproject.user.dto;

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
  private String token;
  private List<Map<String, Object>> applyList;

  public static LoginResponseDto of(User user, String token, List<Map<String, Object>> eduList, List<Map<String, Object>> applyList) {
    return LoginResponseDto.builder()
        .userId(user.getUserId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .userImgUrl(user.getUserImgUrl())
        .phone(user.getPhoneNumber())
        .eduList(eduList)
        .alarmCount(5)
        .token(token)
        .applyList(applyList)
        .build();
  }
}

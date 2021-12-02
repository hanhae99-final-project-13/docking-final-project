package com.sparta.dockingfinalproject.user.dto.response;

import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.user.model.User;
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
  private List<String> alarmContents;
  private TokenDto token;
  private List<Long> requestedPostList;


  public static LoginResponseDto of(User user, TokenDto token, List<Map<String, Object>> eduList, List<String> alarmContents, List<Long> requestedPostList) {
    return LoginResponseDto.builder()
        .userId(user.getUserId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .userImgUrl(user.getUserImgUrl())
        .phone(user.getPhoneNumber())
        .eduList(eduList)
        .alarmCount(alarmContents.size())
        .alarmContents(alarmContents)
        .token(token)
        .requestedPostList(requestedPostList)
        .build();
  }
}

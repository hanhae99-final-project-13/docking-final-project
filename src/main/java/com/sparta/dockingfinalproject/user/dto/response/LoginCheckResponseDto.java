package com.sparta.dockingfinalproject.user.dto.response;

import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCheckResponseDto {

  private Long userId;
  private String nickname;
  private String email;
  private String userImgUrl;
  private String phone;
  private List<Map<String, Object>> eduList;
  private int alarmCount;
  private List<Long> requestedPostList;


  public static LoginCheckResponseDto of(UserDetailsImpl userDetails,
	  List<Map<String, Object>> eduList, int alarmCount, List<Long>requestedPostList) {

	return LoginCheckResponseDto.builder()
		.userId(userDetails.getUser().getUserId())
		.nickname(userDetails.getUser().getNickname())
		.email(userDetails.getUser().getEmail())
		.userImgUrl(userDetails.getUser().getUserImgUrl())
		.phone(userDetails.getUser().getPhoneNumber())
		.eduList(eduList)
		.alarmCount(alarmCount)
		.requestedPostList(requestedPostList)
		.build();
  }

}

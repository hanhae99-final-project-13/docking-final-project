package com.sparta.dockingfinalproject.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KakaoUserInfoDto {

    private Long id;
    private String nickname;
    private String email;
    private String username;

}

package com.sparta.dockingfinalproject.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignupRequestDto {


    private String username;
    private String password;
    private String pwcheck;
    private String email;
    private String nickname;
    private String userImgUrl;
    private Integer randomNumber;
    private String phoneNumber;

}

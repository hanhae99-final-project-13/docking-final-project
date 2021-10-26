package com.sparta.dockingfinalproject.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String username;

    private String password;

    private String pwcheck;

    private String email;

    private String nickname;



}

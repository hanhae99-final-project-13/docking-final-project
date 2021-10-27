package com.sparta.dockingfinalproject.user.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ResponseDto {

    private String nickname;
    private String email;
    private String token;
    private int classCount;
    private int alarmCount;
}

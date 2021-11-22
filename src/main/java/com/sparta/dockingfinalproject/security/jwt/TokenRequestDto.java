package com.sparta.dockingfinalproject.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
  private String accessToken;
  private String refreshToken;
  private String username;


}

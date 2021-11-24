package com.sparta.dockingfinalproject.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import antlr.Token;
import com.sparta.dockingfinalproject.alarm.AlarmRepository;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.token.RefreshToken;
import com.sparta.dockingfinalproject.token.RefreshTokenRepository;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scripting.support.RefreshableScriptTargetSource;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)//진짜 Mock 객체를 만들어줌
class UserServiceTest {

  @InjectMocks
  private UserService userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtTokenProvider jwtTokenProvider;
  @Mock
  private EducationRepository educationRepository;
  @Mock
  private AlarmRepository alarmRepository;
  @Mock
  private RefreshTokenRepository refreshTokenRepository;


  private User user;
  private RefreshToken refreshToken;
  private TokenDto tokenDto;
//
//  @BeforeEach
//  public void init() {
////	user = new User(1L, "user1", "aa1234", "홍길동", "sss@naver.com", "", "", 0L, "");
////	tokenDto = new TokenDto("accessToken","refreshToken",18000L);
////	refreshToken = RefreshToken.builder()
////		.key(user.getUsername())
////		.value(tokenDto.getRefreshToken())
////		.build();
//  }


	@Test
	@DisplayName("로그인")
	public void login() {
	  UserRequestDto requestDto = new UserRequestDto("user1", "aa1234");
	  UserService userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider, educationRepository, alarmRepository, refreshTokenRepository);
	  tokenDto = new TokenDto("accessToken","refreshToken",18000L);
	  refreshToken = RefreshToken.builder()
		  .key(requestDto.getUsername())
		  .value("refreshToken")
		  .build();



	  user = new User(1L, "user1", "aa1234", "홍길동", "sss@naver.com", "", "", 0L, "");
	  Education education = new Education(user);

	  when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
	  when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));
	  when(jwtTokenProvider.createToken(requestDto.getUsername(),requestDto.getUsername())).thenReturn(tokenDto);
	  when(educationRepository.findByUser(user)).thenReturn(Optional.of(education)); 

	  userService.login(requestDto);

	 assertEquals(requestDto.getUsername(), user.getUsername());
	 assertEquals(requestDto.getPassword(), user.getPassword());




	}
  }



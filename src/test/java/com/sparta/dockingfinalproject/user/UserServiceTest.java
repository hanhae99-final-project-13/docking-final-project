package com.sparta.dockingfinalproject.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sparta.dockingfinalproject.alarm.AlarmRepository;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.token.RefreshToken;
import com.sparta.dockingfinalproject.token.RefreshTokenRepository;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UpdateRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import com.sparta.dockingfinalproject.user.dto.response.LoginCheckResponseDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
  private UserDetailsImpl userDetails;
  private Education education;

  private List<Map<String, Object>> educationList;
  private List<Long> requestedPostList;

  @BeforeEach
  public void init() {
	user = new User(1L, "user1", "aa1234", "홍길동", "sss@naver.com", "", "imgurl", 0L, "");
	tokenDto = new TokenDto("accessToken", "refreshToken", 18000L);
	userDetails = new UserDetailsImpl(user);
	education = new Education(1L, user, false, false, false);

	LoginCheckResponseDto.builder()
		.userId(1L)
		.nickname("홍길동")
		.email("sss@naver.com")
		.userImgUrl("imgurl")
		.phone("")
		.eduList(educationList)
		.alarmCount(1)
		.requestedPostList(requestedPostList)
		.build();
  }


  @Test
  @DisplayName("로그인체크 - 성공케이스 ")
  @Disabled
  public void loginCheck() {

	when(educationRepository.findByUser(user)).thenReturn(Optional.of(education));

  }


  @Test
  @DisplayName("회원가입")
  void registerUser() {

	SignupRequestDto requestDto = new SignupRequestDto("user1", "aa1234", "aa1234", "sss@naver.com",
		"홍길동", "https://gorokke.shop/image/profileDefaultImg.jpg", 1234, "");
	User user = new User(requestDto, requestDto.getPassword());

	when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("aa1234");

	userService.registerUser(requestDto);

	assertEquals(user.getUsername(), requestDto.getUsername());
	assertEquals(user.getPassword(), requestDto.getPassword());
	assertEquals(user.getUserImgUrl(), requestDto.getUserImgUrl());

  }

  @Test
  @DisplayName("로그인")
  void login() {
	UserRequestDto requestDto = new UserRequestDto("user1", "aa1234");

	refreshToken = RefreshToken.builder()
		.key(requestDto.getUsername())
		.value("refreshToken")
		.build();

	Education education = new Education(user);
	when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
	when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));
	when(jwtTokenProvider.createToken(requestDto.getUsername(),
		requestDto.getUsername())).thenReturn(tokenDto);
	when(educationRepository.findByUser(user)).thenReturn(Optional.of(education));

	userService.login(requestDto);

	assertEquals(requestDto.getUsername(), user.getUsername());
	assertEquals(requestDto.getPassword(), user.getPassword());
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호가 틀릴경우")
  void login_pw() {
	UserRequestDto requestDto = new UserRequestDto("user1", "bb1234");

	when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(false);
	when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));

	DockingException exception = assertThrows(DockingException.class, () -> {
	  userService.login(requestDto);
	});

	assertEquals(exception.getErrorCode(), ErrorCode.PASSWORD_NOT_FOUND);
  }

  @Test
  @DisplayName("로그인실패 - 아이디가 틀린경우")
  void login_username() {
	UserRequestDto requestDto = new UserRequestDto("user2", "aa1234");
	when(userRepository.findByUsername(requestDto.getUsername()))
		.thenThrow(new DockingException(ErrorCode.USERNAME_NOT_FOUND));

	DockingException exception = assertThrows(DockingException.class, () -> {
	  userService.login(requestDto);
	});
	assertEquals(exception.getErrorCode(), ErrorCode.USERNAME_NOT_FOUND);

  }

  @Test
  @DisplayName("회원 정보 수정")
  void updateUser() {
	UpdateRequestDto requestDto = new UpdateRequestDto("지은짱", "imgurl2");
	when(userRepository.findById(userDetails.getUser().getUserId())).thenReturn(Optional.of(user));

	userService.updateUser(userDetails, requestDto);

	assertEquals(userDetails.getUser().getNickname(), "지은짱");
	assertEquals(userDetails.getUser().getUserImgUrl(), "imgurl2");
  }

}

//ctrl+shift +F10
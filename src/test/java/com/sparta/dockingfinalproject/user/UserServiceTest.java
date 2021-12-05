package com.sparta.dockingfinalproject.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.education.model.Education;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.FosterFormRepository;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.token.RefreshToken;
import com.sparta.dockingfinalproject.token.RefreshTokenRepository;
import com.sparta.dockingfinalproject.user.dto.request.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.request.UpdateRequestDto;
import com.sparta.dockingfinalproject.user.dto.request.UserRequestDto;
import com.sparta.dockingfinalproject.user.model.User;
import com.sparta.dockingfinalproject.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
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
  @Mock
  private FosterFormRepository fosterFormRepository;


  private User user;
  private RefreshToken refreshToken;
  private TokenDto tokenDto;
  private UserDetailsImpl userDetails;
  private List<Long> postIdList;


  @BeforeEach
  public void init() {

	user = new User(1L, "user1", "aaa12345", "홍길동", "sss@naver.com", "", "umgurl", 0L, "");
	tokenDto = new TokenDto("accessToken", "refreshToken", 18000L);
	userDetails = new UserDetailsImpl(user);



	FosterFormRequestDto fosterFormRequestDto = new FosterFormRequestDto("", 1L, "m", "", "", "",
		"", "", "", "", "", "", "", "", "", "", "", "", "", "");

	Post post = new Post(100L, 0L, null, user, new ArrayList<>(), new ArrayList<>(),
		new ArrayList<>());
	Post post1 = new Post(101L, 0L, null, user, new ArrayList<>(), new ArrayList<>(),
		new ArrayList<>());
	Post post2 = new Post(102L, 0L, null, user, new ArrayList<>(), new ArrayList<>(),
		new ArrayList<>());
	FosterForm fosterForm = new FosterForm(post, fosterFormRequestDto, null, null);
	FosterForm fosterForm1 = new FosterForm(post1, fosterFormRequestDto, null, null);
	FosterForm fosterForm2 = new FosterForm(post2, fosterFormRequestDto, null, null);

	List<FosterForm> fosterFormList = new ArrayList<>();
	fosterFormList.add(fosterForm);
	fosterFormList.add(fosterForm1);
	fosterFormList.add(fosterForm2);

	List<Long> postIdList = new ArrayList<>();
	postIdList.add(fosterForm.getPost().getPostId());
	postIdList.add(fosterForm1.getPost().getPostId());


  }

  @Nested
  @DisplayName("회원가입 테스트")
  class	registerUser{
	@Test
	@DisplayName("회원가입_정상")
	void registerUser_Normal() {

	  SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa12345", "aaa12345",
		  "sss@naver.com",
		  "홍길동", "umgurl", 1234, "");

	  when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("aa1234");
	  when(userRepository.save(any())).thenReturn(user);

	  userService.registerUser(requestDto);

	  assertEquals(user.getUsername(), requestDto.getUsername());
	  assertEquals(user.getPassword(), requestDto.getPassword());
	  assertEquals(user.getUserImgUrl(), requestDto.getUserImgUrl());

	}

	@Test
	@DisplayName("회원가입_유저네임이 중복된경우")
	void registerUser_usernameDuplicate() {

	  SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa12345", "aaa12345",
		  "sss@naver.com",
		  "홍길동2", "umgurl", 1234, "");

	  when(userRepository.findByUsername("user1"))
		  .thenReturn(Optional.of(user));

	  DockingException exception = assertThrows(DockingException.class, () -> {
		userService.registerUser(requestDto);
	  });

	  assertEquals(exception.getErrorCode(), ErrorCode.USERNAME_DUPLICATE);
	}

	@Test
	@DisplayName("회원가입_닉네임이 중복된 경우")
	void registerUser_nicknameDuplicate() {

	  SignupRequestDto requestDto = new SignupRequestDto("user2", "aaa12345", "aaa12345",
		  "sss@naver.com",
		  "홍길동", "umgurl", 1234, "");

	  when(userRepository.findByNickname("홍길동"))
		  .thenReturn(Optional.of(user));

	  DockingException exception = assertThrows(DockingException.class, () -> {
		userService.registerUser(requestDto);
	  });

	  assertEquals(exception.getErrorCode(), ErrorCode.NICKNAME_DUPLICATE);
	}

	@Test
	@DisplayName("회원가입_이메일이 중복된 경우")
	void registerUser_emailDuplicate() {
	  SignupRequestDto requestDto = new SignupRequestDto("user2", "aaa12345", "aaa12345",
		  "sss@naver.com", "홍길동2", "umgurl", 1234, "");

	  when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));

	  DockingException exception = assertThrows(DockingException.class, () -> {
		userService.registerUser(requestDto);
	  });
	  assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_DUPLICATE);
	}


	@Test
	@DisplayName("회원가입_비밀번호 중복체크 실패일 경우")
	void registerUser_pw_NotEqual() {
	  SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa12345", "bbb12345",
		  "sss@naver.com", "홍길동", "umgurl", 123
		  , "");

	  DockingException exception = assertThrows(DockingException.class, () -> {
		userService.registerUser(requestDto);
	  });

	  assertEquals(exception.getErrorCode(), ErrorCode.PASSWORD_MISS_MATCH);
	}

	@Test
	@DisplayName("회원가입_이메일 양식이 틀릴 경우")
	void registerUser_emailValid() {
	  SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa12345", "aaa12345",
		  "sssnaver.com", "홍길동", "umgurl", 123, "");
	  DockingException exception = assertThrows(DockingException.class, () -> {
		userService.registerUser(requestDto);
	  });

	  assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_NO_AVAILABILITY);
	}

	@Test
	@DisplayName("회원가입_비밀번호 양식이 틀릴 경우")
	void registerUser_pwValid() {
	  SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa", "aaa", "sss@naver.com",
		  "홍길동", "umgurl", 123, "");

	  DockingException exception = assertThrows(DockingException.class, () -> {
		userService.registerUser(requestDto);
	  });

	  assertEquals(exception.getErrorCode(), ErrorCode.PASSWORD_NOT_AVALABILITY);

	}




  }


  ////////////////////////////////////////////////////////////////////////////////////////////////////////
@Nested
@DisplayName("로그인 테스트")
  class login{

	@Test
	@DisplayName("로그인_정상")
	void login_Normal() {
	  UserRequestDto requestDto = new UserRequestDto("user1", "aaa12345");

	  refreshToken = RefreshToken.builder()
		  .key(requestDto.getUsername())
		  .value("refreshToken")
		  .build();
	  Education education = new Education(1L,user,false,false,false);

	  when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
	  when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));
	  when(jwtTokenProvider.createToken(requestDto.getUsername(),
		  requestDto.getUsername())).thenReturn(tokenDto);
	  when(educationRepository.findByUser(user)).thenReturn(Optional.of(education));
	  when(userRepository.getPostIdFromFosterForm(user)).thenReturn(postIdList);

	  userService.login(requestDto);

	  assertEquals(requestDto.getUsername(), user.getUsername());
	  assertEquals(requestDto.getPassword(), user.getPassword());
	}

	@Test
	@DisplayName("로그인 실패_비밀번호가 틀릴경우")
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
	@DisplayName("로그인실패_아이디가 틀린경우")
	void login_username() {
	  UserRequestDto requestDto = new UserRequestDto("user2", "aa1234");
	  when(userRepository.findByUsername(requestDto.getUsername()))
		  .thenThrow(new DockingException(ErrorCode.USERNAME_NOT_FOUND));

	  DockingException exception = assertThrows(DockingException.class, () -> {
		userService.login(requestDto);
	  });
	  assertEquals(exception.getErrorCode(), ErrorCode.USERNAME_NOT_FOUND);

	}

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

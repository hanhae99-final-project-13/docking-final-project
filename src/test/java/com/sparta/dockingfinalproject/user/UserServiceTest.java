package com.sparta.dockingfinalproject.user;

import com.sparta.dockingfinalproject.alarm.AlarmRepository;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.FosterFormRepository;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.token.RefreshToken;
import com.sparta.dockingfinalproject.token.RefreshTokenRepository;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UpdateRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
	@Mock
	private FosterFormRepository fosterFormRepository;


	private User user;
	private RefreshToken refreshToken;
	private TokenDto tokenDto;
	private UserDetailsImpl userDetails;
//


	@BeforeEach
	public void init() {
		user = new User(1L, "user1", "aaa12345", "홍길동", "sss@naver.com", "", "umgurl", 0L, "");
		tokenDto = new TokenDto("accessToken", "refreshToken", 18000L);
		userDetails = new UserDetailsImpl(user);
	}

	@Test
	@DisplayName("회원가입")
	void registerUser() {

		SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa12345", "aaa12345", "sss@naver.com",
				"홍길동", "https://gorokke.shop/image/profileDefaultImg.jpg", 1234, "");
		User user = new User(requestDto, requestDto.getPassword());

		when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("aa1234");

		userService.registerUser(requestDto);

		assertEquals(user.getUsername(), requestDto.getUsername());
		assertEquals(user.getPassword(), requestDto.getPassword());
		assertEquals(user.getUserImgUrl(), requestDto.getUserImgUrl());

	}

	@Test
	@DisplayName("회원가입 유저네임이 중복된경우")
	void registerUser1() {

		SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa12345", "aaa12345", "sss@naver.com",
				"홍길동", "https://gorokke.shop/image/profileDefaultImg.jpg", 1234, "");
		User user = new User(requestDto, requestDto.getPassword());

		when(userRepository.findByUsername("user1"))
				.thenReturn(Optional.of(user));


		DockingException exception = assertThrows(DockingException.class, () -> {
			userService.registerUser(requestDto);
		});

		assertEquals(exception.getErrorCode(), ErrorCode.USERNAME_DUPLICATE);
	}

	@Test
	@DisplayName("회원가입 닉네임이 중복된경우")
	void registerUser2() {

		SignupRequestDto requestDto = new SignupRequestDto("user1", "aaa12345", "aaa12345", "sss@naver.com",
				"홍길동", "https://gorokke.shop/image/profileDefaultImg.jpg", 1234, "");
		User user = new User(requestDto, requestDto.getPassword());

		when(userRepository.findByUsername("user1"))
				.thenReturn(Optional.of(user));


		DockingException exception = assertThrows(DockingException.class, () -> {
			userService.registerUser(requestDto);
		});

		assertEquals(exception.getErrorCode(), ErrorCode.USERNAME_DUPLICATE);
	}


	@Test
	@DisplayName("로그인")
	void login() {
		UserRequestDto requestDto = new UserRequestDto("user1", "aaa12345");

		refreshToken = RefreshToken.builder()
				.key(requestDto.getUsername())
				.value("refreshToken")
				.build();

		Education education = new Education(user);


		FosterFormRequestDto fosterFormRequestDto = new FosterFormRequestDto("", 1L, "m", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","");

		Post post = new Post(100L, 0L, null, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Post post1 = new Post(101L, 0L, null, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		Post post2 = new Post(102L, 0L, null, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		FosterForm fosterForm = new FosterForm(post, fosterFormRequestDto, null, null);
		FosterForm fosterForm1 = new FosterForm(post1, fosterFormRequestDto, null, null);
		FosterForm fosterForm2 = new FosterForm(post2, fosterFormRequestDto, null, null);

		List<FosterForm> fosterFormList = new ArrayList<>();
		fosterFormList.add(fosterForm);
		fosterFormList.add(fosterForm1);
		fosterFormList.add(fosterForm2);


		when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
		when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));
		when(jwtTokenProvider.createToken(requestDto.getUsername(),
				requestDto.getUsername())).thenReturn(tokenDto);
		when(educationRepository.findByUser(user)).thenReturn(Optional.of(education));

		when(fosterFormRepository.findAllByUser(user)).thenReturn(fosterFormList);

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


	//
//    @Test
//    @Disabled
//    @DisplayName("로그인체크")
//    public void loginCheck() {
//        User user = userDetails.getUser();
//
//        List<Alarm> alarms = new ArrayList<>();
//        Alarm alarm1 = new Alarm();
//        Alarm alarm2 = new Alarm();
//        alarms.add(alarm1);
//        alarms.add(alarm2);
//
//
//        Education education = new Education(1L, user, false, false, false);
//        FosterFormRequestDto fosterFormRequestDto = new FosterFormRequestDto("", 1L, "m", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
//
//
//        Post post = new Post(100L, 0L, null, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        Post post1 = new Post(101L, 0L, null, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        Post post2 = new Post(102L, 0L, null, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        FosterForm fosterForm = new FosterForm(post, fosterFormRequestDto, null, null);
//        FosterForm fosterForm1 = new FosterForm(post1, fosterFormRequestDto, null, null);
//        FosterForm fosterForm2 = new FosterForm(post2, fosterFormRequestDto, null, null);
//        List<FosterForm> fosterFormList = new ArrayList<>();
//        fosterFormList.add(fosterForm);
//        fosterFormList.add(fosterForm1);
//        fosterFormList.add(fosterForm2);
//
//
//        userService.loginCheck(userDetails);
//        when(alarmRepository.findAllByUserAndCheckedTrueOrderByCreatedAtDesc(user)).thenReturn(alarms);
//        when(educationRepository.findByUser(any(User.class))).thenReturn(Optional.of(education));
//        when(fosterFormRepository.findAllByUser(user)).thenReturn(fosterFormList);
//
//        Map<String, Object> data = (Map<String, Object>) userService.loginCheck(userDetails).get("data");
//
//        assertEquals(education.getAdvanced(), false);
//
//    }


}

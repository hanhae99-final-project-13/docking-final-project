//package com.sparta.dockingfinalproject.user;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//
//import com.sparta.dockingfinalproject.alarm.AlarmRepository;
//import com.sparta.dockingfinalproject.education.EducationRepository;
//import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
//import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
//import java.util.Optional;
//import org.junit.Before;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@ExtendWith(MockitoExtension.class)//진짜 Mock 객체를 만들어줌
//class UserServiceTest {
//
//  @Mock
//  UserRepository userRepository;
//  @Mock
//  PasswordEncoder passwordEncoder;
//  @Mock
//  JwtTokenProvider jwtTokenProvider;
//  @Mock
//  EducationRepository educationRepository;
//  @Mock
//  AlarmRepository alarmRepository;
//
//  @Before
//  public void setup() {
//	SignupRequestDto reuqestDto = new SignupRequestDto(
//		"wldms", "thd123", "thd123", "wldms@email.com", "지은님", "imgurl", 1234, "010-1234-5678");
////	SignupRequestDto.builder();
//
//  }
//
//  @Test
//  @DisplayName("로그인")
//  void login() {
//	SignupRequestDto requestDto = new SignupRequestDto();
//
//	//given
//	UserService userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider,
//		educationRepository, alarmRepository);
//
//	User user = new User();
////	user.setUserId(1L);
//	user.setUsername("wldms");
//	user.setPassword("thd123");
////	user.setNickname("지은님");
////	user.setEmail("wldms2494@email.com");
////	user.setBadge("silver");
////	user.setUserImgUrl("imageUrl");
////	user.setKakaoId(2L);
////	user.setPhoneNumber("01012345678");
//
//	given(userRepository.findByUsername("wldms")).willReturn(Optional.of(user));
//
//	//when
//	userService.login(requestDto);
//
//	//then
//	assertEquals(user.getUsername(),requestDto.getUsername());
//	then(userService).should().login(requestDto);
//
//
//
//  }
//
//}
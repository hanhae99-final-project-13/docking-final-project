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
//import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
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
//
//
//	UserRequestDto requestDto = new UserRequestDto("wldms", "thd123");
//
//  }
//
//  @Test
//  @DisplayName("로그인")
//  void login() {
//	UserRequestDto requestDto = new UserRequestDto();
//
//	//given
//	UserService userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider,
//		educationRepository, alarmRepository);
//
//	User user = new User();
////	user.setUserId(1L);
//	user.setUsername("wldms");
//	user.setPassword("thd123");
////
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
//package com.sparta.dockingfinalproject.education;
//
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assumptions.assumingThat;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.when;
//
//import com.sparta.dockingfinalproject.security.UserDetailsImpl;
//import com.sparta.dockingfinalproject.user.User;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
// class EducationServiceTest {
//
//  @InjectMocks
//  private EducationService educationService;
//  @Mock
//  private EducationRepository educationRepository;
//
//  private User user;
//  private UserDetailsImpl userDetails;
//  private Education education;
//  private String edu;
//
//  @BeforeEach
//   void setUp(){
//	user = new User(1L, "user1", "aa1234", "홍길동", "sss@naver.com", "", "umgurl", 0L, "");
//	userDetails = new UserDetailsImpl(user);
//	education = new Education(1L, user, false, false, false);
//
//  }
//
//  @Test
//  @DisplayName("save education class")
//  void saveEdu() {
//
//	assumingThat(edu.equals("1"),()->{
//
//	})
//
//  }
//}
//

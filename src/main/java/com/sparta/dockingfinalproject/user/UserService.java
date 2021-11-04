package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.PhoneRequestDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import com.sparta.dockingfinalproject.user.mail.MailSendService;
import com.sparta.dockingfinalproject.user.phoneMessage.PhoneService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final MailSendService mailSendService;
  private final PhoneService phoneService;


  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
	  JwtTokenProvider jwtTokenProvider, MailSendService mailSendService,
	  PhoneService phoneService) {
	this.userRepository = userRepository;
	this.passwordEncoder = passwordEncoder;
	this.jwtTokenProvider = jwtTokenProvider;
	this.mailSendService = mailSendService;
	this.phoneService = phoneService;


  }

  //회원 등록
  public Map<String, Object> registerUser(SignupRequestDto requestDto) {

	String username = requestDto.getUsername();
	String password = requestDto.getPassword();
	String pwcheck = requestDto.getPwcheck();
	String nickname = requestDto.getNickname();
	String email = requestDto.getEmail();
	String userImgUrl = "이미지url";
	String authKey = mailSendService.sendSimpleMessage(email);


	if (username.isEmpty()) {
	  throw new DockingException(ErrorCode.USERNAME_NOT_FOUND);
	}
	if (email.isEmpty()) {
	  throw new DockingException(ErrorCode.EMAIL_NOT_FOUND);
	}
	if (nickname.isEmpty()) {
	  throw new DockingException(ErrorCode.NICKNAME_NOT_FOUND);
	}
	if (!password.equals(pwcheck)) {
	  throw new DockingException(ErrorCode.PASSWORD_MISS_MATCH);
	}

//	if(!requestDto.isAuthCheck() == true  ) {
//	  throw new DockingException(ErrorCode.EMAIL_NOT_FOUND);
//	}
	//패스워드 인코딩 완료
	password = passwordEncoder.encode(password);

	User user = new User(username, password, nickname, email, userImgUrl, authKey);
	userRepository.save(user);

	//data에 메세지넣기
	Map<String, Object> data = new HashMap<>();
	data.put("msg", "회원가입이 완료되었습니다");

	return SuccessResult.success(data);

  }


  //로그인
  //아이디 불일치일때 예외처리는?
  public Map<String, Object> login(SignupRequestDto requestDto) {
	User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);
	//      User user = userRepository.findAllByAuthCheckTrueAndUsername(requestDto.getUsername()).orElseThrow(
//          () -> new IllegalArgumentException("이메일 인증 부터 해주세요")
//      );



	List<Map<String, Object>> applyList = new ArrayList<>();
	Map<String, Object> apply = new HashMap<>();

	if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
	  throw new DockingException(ErrorCode.USERNAME_MISS_MATCH);
	}

	Map<String, Object> data = new HashMap<>();
	data.put("nickname", user.getNickname());
	data.put("email", user.getEmail());
	data.put("userImgUrl", user.getUserImgUrl());
	data.put("classCount", 5);
	data.put("alarmCount", 5);
	data.put("token",
		jwtTokenProvider.createToken(requestDto.getUsername(), requestDto.getUsername()));
	data.put("applyList", applyList);

	apply.put("applyState", "디폴트");
	apply.put("postId", "디폴트");

	applyList.add(apply);

	return SuccessResult.success(data);
  }

  //회원정보 수정
  @Transactional
  public Map<String, Object> updateUser(UserDetailsImpl userDetails, SignupRequestDto requestDto) {

	User findUser = userRepository.findById(userDetails.getUser().getUserId()).orElseThrow(
		() -> new DockingException(ErrorCode.USER_NOT_FOUND)
	);
	findUser.update(requestDto);
	//리턴 data 생성
	Map<String, String> data = new HashMap<>();
	data.put("msg", "사용자 정보가 수정 되었습니다");
	return SuccessResult.success(data);
  }

//로그인 체크

  public Map<String, Object> loginCheck(UserDetailsImpl userDetails) {
	Map<String, Object> data = new HashMap<>();

	if (userDetails != null) {
	  List<Map<String, Object>> applyList = new ArrayList<>();

	  Map<String, Object> apply = new HashMap<>();

	  data.put("nickname", userDetails.getUser().getNickname());
	  data.put("email", userDetails.getUser().getEmail());
	  data.put("userImgUrl", userDetails.getUser().getUserImgUrl());
	  data.put("classCount", 5);
	  data.put("alarmCount", 5);
	  data.put("applyList", applyList);

	  apply.put("applyState", "디폴트");
	  apply.put("postId", "디폴트");

	  applyList.add(apply);
	} else {
	  throw new DockingException(ErrorCode.USER_NOT_FOUND);
	}

	System.out.println("로그인 체크완료");
	return SuccessResult.success(data);

  }


  public Map<String, Object> idDoubleCheck(String username) {
	//빈값은 프론트에서 처리됨

	Map<String, Object> data = new HashMap<>();
	Optional<User> found = userRepository.findByUsername(username);

	if (!found.isPresent()) {
	  data.put("msg", "아이디 중복 확인 완료");
	} else {
	  throw new DockingException(ErrorCode.USERNAME_DUPLICATE);
	}

	return SuccessResult.success(data);

  }


  public Map<String, Object> nicknameDoubleCheck(String nickname) {

	Map<String, Object> data = new HashMap<>();
	Optional<User> found = userRepository.findByNickname(nickname);

	if (!found.isPresent()) {
	  data.put("msg", "닉네임 중복 확인 완료");
	} else {
	  throw new DockingException(ErrorCode.NICKNAME_DUPLICATE);
	}

	return SuccessResult.success(data);


  }


  //이메일 인증 확인
  @Transactional
  public void singUpConfirm(String email, String authKey) throws Exception {
	User user = userRepository.findByEmail(email).orElseThrow(
		() -> new IllegalArgumentException("인증번호가 만료되었습니다. 다시 회원가입 해주세요")
	);
	if (user.getAuthKey().equalsIgnoreCase(authKey)) {
	  user.confirm();
	} else {
	  throw new DockingException(ErrorCode.POST_NOT_FOUND);
	}

  }

  //휴대폰 인증 확인

  public Map<String, Object> phoneConfirm(UserDetailsImpl userDetails, PhoneRequestDto requestDto) {
	Map<String, Object> data = new HashMap<>();


	User user = userRepository.findByUsername(userDetails.getUser().getUsername()).orElseThrow(
		() -> new DockingException(ErrorCode.USER_NOT_FOUND)
	);

	System.out.println("클라이언트인증번호 " + requestDto.getRandomNumber());

// 	String randomNumber2 ="1234";
	int randomNumber2=phoneService.sendMessage(requestDto);
	// randomNumber2 = 생성된 인증번호 1234, randomnumber 는 clien로 부터 온 번호
	if (requestDto.getRandomNumber() == (randomNumber2)) {


	  user.setRandomNumber(requestDto.getRandomNumber());
	  userRepository.save(user);
	  data.put("msg", "인증번호가 일치합니다 ");


	} else {
	  throw new DockingException(ErrorCode.NUMBER_MISS_MATCH);
	}
	return SuccessResult.success(data);
  }

}



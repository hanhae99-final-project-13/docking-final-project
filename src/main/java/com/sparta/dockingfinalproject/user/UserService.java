package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.alarm.Alarm;
import com.sparta.dockingfinalproject.alarm.AlarmRepository;
import com.sparta.dockingfinalproject.common.SuccessResult;
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
import com.sparta.dockingfinalproject.user.dto.UserInquriryRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import com.sparta.dockingfinalproject.user.dto.response.LoginCheckResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final EducationRepository educationRepository;
  private final AlarmRepository alarmRepository;
  private final RefreshTokenRepository refreshTokenRepository;


  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
	  JwtTokenProvider jwtTokenProvider,
	  EducationRepository educationRepository, AlarmRepository alarmRepository, RefreshTokenRepository refreshTokenRepository) {

	this.userRepository = userRepository;
	this.passwordEncoder = passwordEncoder;
	this.jwtTokenProvider = jwtTokenProvider;
	this.educationRepository = educationRepository;
	this.alarmRepository = alarmRepository;
	this.refreshTokenRepository = refreshTokenRepository;
  }

  //회원 등록
  public Map<String, Object> registerUser(SignupRequestDto requestDto) {
	validateUser(requestDto);

	//패스워드 인코딩
	String password = requestDto.getPassword();
	password = passwordEncoder.encode(password);

	User user = new User(requestDto, password);
	userRepository.save(user);

	Education education = new Education(user);
	educationRepository.save(education);

	Alarm alarm = new Alarm("회원가입을 축하합니다");
	alarm.addUser(user);
	alarmRepository.save(alarm);

	Map<String, Object> data = new HashMap<>();
	data.put("msg", "회원가입 축하합니다!");

	return SuccessResult.success(data);
  }


  //로그인
  @Transactional
  public Map<String, Object> login(UserRequestDto requestDto) {

	TokenDto tokenDto = jwtTokenProvider.createToken(requestDto.getUsername(),
		requestDto.getUsername());

	//리프레시 토큰을 저장하기.
	RefreshToken refreshToken = RefreshToken.builder()
		.key(requestDto.getUsername())
		.value(tokenDto.getRefreshToken())
		.build();

	refreshTokenRepository.save(refreshToken);

	User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);

	String username = requestDto.getUsername();
	usernameEmpty(username);

	if (requestDto.getPassword().isEmpty()) {
	  throw new DockingException(ErrorCode.PASSWORD_EMPTY);
	}

	if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
	  throw new DockingException(ErrorCode.PASSWORD_MISS_MATCH);
	}

	Map<String, Object> data = new HashMap<>();

	data.put("nickname", user.getUserId());
	data.put("email", user.getEmail());
	data.put("userImgUrl", user.getUserImgUrl());
	data.put("alarmCount", 5);
	data.put("token", tokenDto.getAccessToken());
	data.put("refreshToken", tokenDto.getRefreshToken());

	List<Map<String, Object>> eduList = getEduList(user);
	data.put("eduList", eduList);

	return SuccessResult.success(data);
  }


  //회원정보 수정
  @Transactional
  public Map<String, Object> updateUser(UserDetailsImpl userDetails, UpdateRequestDto requestDto) {
//	nicknameEmpty(requestDto.getNickname());

	User findUser = userRepository.findById(userDetails.getUser().getUserId()).orElseThrow(
		() -> new DockingException(ErrorCode.USER_NOT_FOUND)
	);
	findUser.update(requestDto);

	Map<String, String> data = new HashMap<>();
	data.put("msg", "사용자 정보가 수정 되었습니다");
	return SuccessResult.success(data);
  }


  //로그인 체크
  public Map<String, Object> loginCheck(UserDetailsImpl userDetails) {

	User user = userDetails.getUser();

	if (userDetails != null) {

	  List<Map<String, Object>> eduList = getEduList(user);

	  LoginCheckResponseDto loginCheckResponseDto = LoginCheckResponseDto.of(
		  userDetails, eduList);
	  return SuccessResult.success(loginCheckResponseDto);

	} else {
	  throw new DockingException(ErrorCode.USER_NOT_FOUND);
	}


  }


  //아이디 중복 체크
  public Map<String, Object> idDoubleCheck(String username) {

	Map<String, Object> data = new HashMap<>();
	Optional<User> found = userRepository.findByUsername(username);

	usernameEmpty(username);

	if (!found.isPresent()) {
	  data.put("msg", "아이디 중복 확인 완료");
	} else {
	  throw new DockingException(ErrorCode.USERNAME_DUPLICATE);
	}

	return SuccessResult.success(data);

  }


  //닉네임 중복 체크
  public Map<String, Object> nicknameDoubleCheck(String nickname) {
	Map<String, Object> data = new HashMap<>();
	Optional<User> found = userRepository.findByNickname(nickname);
	nicknameEmpty(nickname);

	if (!found.isPresent()) {
	  data.put("msg", "닉네임 중복 확인 완료");
	} else {
	  throw new DockingException(ErrorCode.NICKNAME_DUPLICATE);
	}
	return SuccessResult.success(data);
  }


  //아이디찾기
  public Map<String, Object> findUserId(UserInquriryRequestDto userInquriryRequestDto) {
	String email = userInquriryRequestDto.getEmail();
	User findUser = userRepository.findByEmail(email).orElseThrow(
		() -> new DockingException(ErrorCode.EMAIL_NOT_FOUND)
	);

	Map<String, String> data = new HashMap<>();
	data.put("username", findUser.getUsername());
	return SuccessResult.success(data);
  }


  //비밀번호찾기
  @Transactional
  public Map<String, Object> findUserPw(UserInquriryRequestDto userInquriryRequestDto,
	  String tempPw) {
	String username = userInquriryRequestDto.getUsername();
	User user = userRepository.findByUsername(username).orElseThrow(
		() -> new DockingException(ErrorCode.USER_NOT_FOUND)
	);
	user.setPassword(tempPw);

	Map<String, String> data = new HashMap<>();
	data.put("msg", "임시 비밀번호를 해당 이메일로 보냈습니다.");
	return SuccessResult.success(data);
  }


  private void validateUser(SignupRequestDto requestDto) {
	String username = requestDto.getUsername();
	String password = requestDto.getPassword();
	String pwcheck = requestDto.getPwcheck();
	String nickname = requestDto.getNickname();

	usernameEmpty(username);
	nicknameEmpty(nickname);

	Optional<User> findUser = userRepository.findByUsername(username);
	if (findUser.isPresent()) {
	  throw new DockingException(ErrorCode.USERNAME_DUPLICATE);
	}

	Optional<User> findUser2 = userRepository.findByNickname(nickname);
	if (findUser2.isPresent()) {
	  throw new DockingException(ErrorCode.NICKNAME_DUPLICATE);
	}

	Optional<User> findUser3 = userRepository.findByEmail(requestDto.getEmail());
	if (findUser3.isPresent()) {
	  throw new DockingException(ErrorCode.EMAIL_DUPLICATE);
	}

	if (!password.equals(pwcheck)) {
	  throw new DockingException(ErrorCode.PASSWORD_MISS_MATCH);
	}
  }

  private void usernameEmpty(String username) {
	if (username.isEmpty()) {
	  throw new DockingException(ErrorCode.USERNAME_EMPTY);
	}
  }

  private void nicknameEmpty(String nickname) {
	if (nickname.isEmpty()) {
	  throw new DockingException(ErrorCode.NICKNAME_EMPTY);
	}
  }

  private List<Map<String, Object>> getEduList(User user) {
	Education education = educationRepository.findByUser(user).orElse(null);
	List<Map<String, Object>> eduList = new ArrayList<>();
	Map<String, Object> edu = new HashMap<>();
	edu.put("필수지식", education.getBasic());
	edu.put("심화지식", education.getAdvanced());
	edu.put("심화지식2", education.getCore());
	eduList.add(edu);
	return eduList;
  }


}



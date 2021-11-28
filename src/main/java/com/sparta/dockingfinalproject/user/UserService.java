package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.alarm.AlarmRepository;
import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.FosterFormRepository;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtReturn;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.security.jwt.TokenRequestDto;
import com.sparta.dockingfinalproject.token.RefreshToken;
import com.sparta.dockingfinalproject.token.RefreshTokenRepository;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UpdateRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserInquriryRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import com.sparta.dockingfinalproject.user.dto.response.LoginCheckResponseDto;
import com.sparta.dockingfinalproject.user.dto.response.LoginResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
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
  private final FosterFormRepository fosterFormRepository;


  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
	  JwtTokenProvider jwtTokenProvider,
	  EducationRepository educationRepository, AlarmRepository alarmRepository,
	  RefreshTokenRepository refreshTokenRepository, FosterFormRepository fosterFormRepository) {

	this.userRepository = userRepository;
	this.passwordEncoder = passwordEncoder;
	this.jwtTokenProvider = jwtTokenProvider;
	this.educationRepository = educationRepository;
	this.alarmRepository = alarmRepository;
	this.refreshTokenRepository = refreshTokenRepository;
	this.fosterFormRepository = fosterFormRepository;
  }

  public Map<String, Object> registerUser(SignupRequestDto requestDto) {
	validateUser(requestDto);

	String password = requestDto.getPassword();
	password = passwordEncoder.encode(password);

	User user = new User(requestDto, password);
	userRepository.save(user);

	Education education = new Education(user);
	educationRepository.save(education);

	String alarmContent = "회원가입을 축하합니다";
	AlarmType alarmType = AlarmType.SIGN_UP;
	Long contentId = null;
	Alarm alarm = new Alarm(alarmContent, alarmType, contentId, user);
	alarmRepository.save(alarm);

	Map<String, Object> data = new HashMap<>();
	data.put("msg", "회원가입 축하합니다!");

	return SuccessResult.success(data);
  }


  @Transactional
  public Map<String, Object> login(UserRequestDto requestDto) {

	User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
		() -> new DockingException(ErrorCode.USERNAME_NOT_FOUND)
	);
	validateLogin(requestDto, user);

	TokenDto tokenDto = jwtTokenProvider.createToken(requestDto.getUsername(),
		requestDto.getUsername());

	saveRefreshToken(requestDto, tokenDto);

	List<Map<String, Object>> eduList = getEduList(user);
	List<String> alarmContents = findUserAlarms(user);
	List<Long> requestedPostList = getRequestedPostList(user);

	LoginResponseDto loginResponseDto = LoginResponseDto.of(
		user, jwtTokenProvider.createToken(requestDto.getUsername(), requestDto.getUsername()),
		eduList, alarmContents, requestedPostList);

	return SuccessResult.success(loginResponseDto);
  }



  @Transactional
  public Map<String, Object> updateUser(UserDetailsImpl userDetails, UpdateRequestDto requestDto) {

	User findUser = userRepository.findById(userDetails.getUser().getUserId()).orElseThrow(
		() -> new DockingException(ErrorCode.USER_NOT_FOUND)
	);
	findUser.update(requestDto);

	Map<String, String> data = new HashMap<>();
	data.put("msg", "사용자 정보가 수정 되었습니다");
	return SuccessResult.success(data);
  }


  public Map<String, Object> loginCheck(UserDetailsImpl userDetails) {

	if (userDetails != null) {
	  User user = userDetails.getUser();
	  int alarmCount = getUserAlarmCount(user);
	  List<Map<String, Object>> eduList = getEduList(user);
	  List<Long> requestedPostList = getRequestedPostList(user);
	  LoginCheckResponseDto loginCheckResponseDto = LoginCheckResponseDto.of(
		  userDetails, eduList, alarmCount, requestedPostList);

	  return SuccessResult.success(loginCheckResponseDto);
	} else {
	  throw new DockingException(ErrorCode.USER_NOT_FOUND);
	}
  }

  @Transactional
  public TokenDto reissue(TokenRequestDto tokenRequestDto) {

	if (jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken()) != JwtReturn.SUCCESS) {
	  throw new DockingException(ErrorCode.LOGIN_TOKEN_EXPIRE);
	}

	String username = jwtTokenProvider.getAccessTokenPayload(tokenRequestDto.getAccessToken());

	RefreshToken refreshToken = refreshTokenRepository.findByKey(username).orElseThrow(
		() -> new DockingException(ErrorCode.LOGIN_REQUIRED));

	if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
	  throw new DockingException(ErrorCode.LOGIN_REQUIRED);
	}
	TokenDto tokenDto = jwtTokenProvider.createToken(username, username);

	RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
	refreshTokenRepository.save(newRefreshToken);

	return tokenDto;

  }


  public Map<String, Object> idDoubleCheck(String username) {

	Map<String, Object> data = new HashMap<>();
	Optional<User> found = userRepository.findByUsername(username);
	usernameEmptyCheck(username);

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
	nicknameEmptyCheck(nickname);

	if (!found.isPresent()) {
	  data.put("msg", "닉네임 중복 확인 완료");
	} else {
	  throw new DockingException(ErrorCode.NICKNAME_DUPLICATE);
	}
	return SuccessResult.success(data);
  }


 public Map<String, Object> findUserId(UserInquriryRequestDto userInquriryRequestDto) {
	String email = userInquriryRequestDto.getEmail();
	User findUser = userRepository.findByEmail(email).orElseThrow(
		() -> new DockingException(ErrorCode.EMAIL_NOT_FOUND)
	);

	Map<String, String> data = new HashMap<>();
	data.put("username", findUser.getUsername());
	return SuccessResult.success(data);
  }


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
	String email = requestDto.getEmail();

	usernameEmptyCheck(username);
	nicknameEmptyCheck(nickname);
	passwordEmptyCheck(password);
	passwordEmptyCheck(pwcheck);
	isEamil(email);

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

	if (isEamil(email) == false) {
	  throw new DockingException(ErrorCode.EMAIL_NO_AVAILABILITY);
	}

	if (isPassword(password) == false) {
	  throw new DockingException(ErrorCode.PASSWORD_NOT_AVALABILITY);
	}

  }


  private void validateLogin(UserRequestDto requestDto, User user) {
	usernameEmptyCheck(requestDto.getUsername());
	passwordEmptyCheck(requestDto.getPassword());

	if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
	  throw new DockingException(ErrorCode.PASSWORD_NOT_FOUND);
	}
  }


  private void usernameEmptyCheck(String username) {
	if (username.isEmpty()) {
	  throw new DockingException(ErrorCode.USERNAME_EMPTY);
	}
  }

  private void nicknameEmptyCheck(String nickname) {
	if (nickname.isEmpty()) {
	  throw new DockingException(ErrorCode.NICKNAME_EMPTY);
	}
  }

  private void passwordEmptyCheck(String password) {
	if (password.isEmpty()) {
	  throw new DockingException(ErrorCode.PASSWORD_EMPTY);
	}
  }

  public boolean isEamil(String email) {
	return Pattern.matches("^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", email);
  }

  public boolean isPassword(String password) {
	return Pattern.matches("^(?=.*[0-9])(?=.*[a-z]).{8,20}$", password);
  }

  private void saveRefreshToken(UserRequestDto requestDto, TokenDto tokenDto) {
	RefreshToken refreshToken = RefreshToken.builder()
		.key(requestDto.getUsername())
		.value(tokenDto.getRefreshToken())
		.build();

	refreshTokenRepository.save(refreshToken);
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

  private List<Long> getRequestedPostList(User user) {
	List<FosterForm> fosterFormList = fosterFormRepository.findAllByUser(user);
	List<Long> requestedPostList = new ArrayList();
	for (FosterForm form : fosterFormList) {
	  Long requestedPostId = form.getPost().getPostId();
	  requestedPostList.add(requestedPostId);
	}
	return requestedPostList;
  }

  private int getUserAlarmCount(User user) {
	return alarmRepository.findAllByUserAndCheckedTrueOrderByCreatedAtDesc(user).size();
  }


  private List<String> findUserAlarms(User user) {
	List<Alarm> allAlarms = alarmRepository
		.findAllByUserOrderByCreatedAtDesc(user);

	List<String> alarmContents = new ArrayList<>();
	for (Alarm alarm : allAlarms) {
	  alarmContents.add(alarm.getAlarmContent());
	}

	return alarmContents;
  }
}



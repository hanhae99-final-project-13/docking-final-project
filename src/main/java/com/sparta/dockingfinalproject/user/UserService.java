package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.LoginCheckResponseDto;
import com.sparta.dockingfinalproject.user.dto.LoginResponseDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UpdateRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserInquriryRequestDto;
import com.sparta.dockingfinalproject.user.mail.MailSendService;
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
  private final MailSendService mailSendService;
  private final EducationRepository educationRepository;


  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
	  JwtTokenProvider jwtTokenProvider, MailSendService mailSendService,
	  EducationRepository educationRepository) {
	this.userRepository = userRepository;
	this.passwordEncoder = passwordEncoder;
	this.jwtTokenProvider = jwtTokenProvider;
	this.mailSendService = mailSendService;
	this.educationRepository = educationRepository;
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

	//data에 메세지넣기
	Map<String, Object> data = new HashMap<>();
	data.put("msg", "회원가입 축하합니다!");

	return SuccessResult.success(data);
  }

  private void validateUser(SignupRequestDto requestDto) {
	String username = requestDto.getUsername();
	String password = requestDto.getPassword();
	String pwcheck = requestDto.getPwcheck();
	String nickname = requestDto.getNickname();

	usernameEmpty(username);

	Optional<User> findUser = userRepository.findByUsername(username);
	if (findUser.isPresent()) {
	  throw new DockingException(ErrorCode.USERNAME_DUPLICATE);
	}

	if (nickname.isEmpty()) {
	  throw new DockingException(ErrorCode.NICKNAME_NOT_FOUND);
	}

	findUser = userRepository.findByNickname(nickname);
	if (findUser.isPresent()) {
	  throw new DockingException(ErrorCode.NICKNAME_DUPLICATE);
	}

	if (!password.equals(pwcheck)) {
	  throw new DockingException(ErrorCode.PASSWORD_MISS_MATCH);
	}
  }

  //로그인
  public Map<String, Object> login(SignupRequestDto requestDto) {
	String username = requestDto.getUsername();
	usernameEmpty(username);

	//패스워드 빈값일때
	if (requestDto.getPassword().isEmpty()) {
	  throw new DockingException(ErrorCode.PASSWORD_EMPTY);
	}

	User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
		() -> new DockingException(ErrorCode.USERNAME_NOT_FOUND)
	);

	//패스워드 불일치일때
	if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
	  throw new DockingException(ErrorCode.PASSWORD_MISS_MATCH);
	}

	Education education = educationRepository.findByUser(user).orElse(null);
	Map<String, Object> data = new HashMap<>();
	List<Map<String, Object>> applyList = new ArrayList<>();
	Map<String, Object> apply = new HashMap<>();

	List<Map<String, Object>> eduList = new ArrayList<>();
	Map<String, Object> edu = new HashMap<>();

	LoginResponseDto loginResponseDto = LoginResponseDto.of(user,
		jwtTokenProvider.createToken(requestDto.getUsername(), requestDto.getUsername()), eduList,
		applyList);

	apply.put("applyState", "디폴트");
	apply.put("postId", "디폴트");
	edu.put("필수지식", education.getBasic());
	edu.put("심화지식", education.getAdvanced());
	edu.put("심화지식2", education.getCore());
	eduList.add(edu);
	applyList.add(apply);

	return SuccessResult.success(loginResponseDto);
  }

  private void usernameEmpty(String username) {
	//아이디가 빈값일때
	if (username.isEmpty()) {
	  throw new DockingException(ErrorCode.USERNAME_EMPTY);
	}
  }

  //회원정보 수정
  @Transactional
  public Map<String, Object> updateUser(UserDetailsImpl userDetails, UpdateRequestDto requestDto) {

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

	User user = userDetails.getUser();

	Education education = educationRepository.findByUser(user).orElse(null);

	if (userDetails != null) {
	  List<Map<String, Object>> applyList = new ArrayList<>();
	  Map<String, Object> apply = new HashMap<>();

	  List<Map<String, Object>> eduList = new ArrayList<>();
	  Map<String, Object> edu = new HashMap<>();

	  LoginCheckResponseDto loginCheckResponseDto = LoginCheckResponseDto.of(
		  userDetails, eduList, applyList);


	  apply.put("applyState", "디폴트");
	  apply.put("postId", "디폴트");
	  edu.put("필수지식", education.getBasic());
	  edu.put("심화지식", education.getAdvanced());
	  edu.put("심화지식2", education.getCore());
	  applyList.add(apply);
	  eduList.add(edu);

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

	if (nickname.isEmpty()) {
	  throw new DockingException(ErrorCode.NICKNAME_NOT_FOUND);
	}
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


}



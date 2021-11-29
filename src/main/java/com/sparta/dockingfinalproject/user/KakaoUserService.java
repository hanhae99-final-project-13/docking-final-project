package com.sparta.dockingfinalproject.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.FosterFormRepository;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.security.jwt.TokenDto;
import com.sparta.dockingfinalproject.token.RefreshToken;
import com.sparta.dockingfinalproject.token.RefreshTokenRepository;
import com.sparta.dockingfinalproject.user.dto.KakaoUserInfoDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoUserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final EducationRepository educationRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final FosterFormRepository fosterFormRepository;

  public KakaoUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider,
      EducationRepository educationRepository, RefreshTokenRepository refreshTokenRepository,
      FosterFormRepository fosterFormRepository) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.educationRepository = educationRepository;
    this.jwtTokenProvider = jwtTokenProvider;
    this.refreshTokenRepository = refreshTokenRepository;
    this.fosterFormRepository = fosterFormRepository;

  }

  public Map<String, Object> kakaoLogin(String code) throws JsonProcessingException {

    if (code == null) {
      throw new DockingException(ErrorCode.CODE_NOT_FOUND);
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    String accessToken = getAccessToken(code);

    // 2. 토큰으로 카카오 API 호출
    KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

    // 3. 필요시에 회원가입
    User kakaoUser = registerKakaoOrUpdateKakao(kakaoUserInfo);

    return SuccessResult.success(forceLogin(kakaoUser));
  }

  //////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////아래는 메소드//////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////

  private String getAccessToken(String code) throws JsonProcessingException {

    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // HTTP Body 생성
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", "b288c56fd31bb6f686ba8a3a39ba7fb2");
    body.add("redirect_uri", "https://getting.co.kr/oauth/callback/kakao");

    System.out.println("현재 코드 값 " + code);
    body.add("code", code);

    //http header와 body를 하나의 오브젝트에 담기
    HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

    //Http 요청하기 - post방식
    RestTemplate rt = new RestTemplate();
    ResponseEntity<String> response = rt.exchange(
        "https://kauth.kakao.com/oauth/token",
        HttpMethod.POST,
        kakaoTokenRequest,
        String.class
    );

    // HTTP 응답 (JSON) -> 액세스 토큰 파싱
    String responseBody = response.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);

    return jsonNode.get("access_token").asText();

  }

  private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

    HttpHeaders headers = new HttpHeaders();
    // Http Header 생성
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

    //Http 요청하기 - Post방식
    RestTemplate rt = new RestTemplate();
    ResponseEntity<String> response = rt.exchange(
        "https://kapi.kakao.com/v2/user/me",
        HttpMethod.POST,
        kakaoUserInfoRequest,
        String.class
    );

    String responseBody = response.getBody();
    responseBody = response.getBody();

    ObjectMapper objectMapper = new ObjectMapper();

    JsonNode jsonNode = objectMapper.readTree(responseBody);
    Long id = jsonNode.get("id").asLong();
    String nickname = jsonNode.get("properties")
        .get("nickname").asText();
    String email = jsonNode.get("kakao_account")
        .get("email").asText();
    String userImgUrl = "https://gorokke.shop/image/profileDefaultImg.jpg";

    //여기가 안불러와짐
//    try {
//      userImgUrl = jsonNode.get("properties")
//          .get("profile_image").asText();
//    } catch (Exception ignored) {
//    }

    System.out.println("카카오 사용자 정보: " + id + ", " + nickname + ", " + email + "," + userImgUrl);

    String username = email;

    return new KakaoUserInfoDto(id, nickname, email, username, userImgUrl);

  }

  private User registerKakaoOrUpdateKakao(KakaoUserInfoDto kakaoUserInfoDto) {
    User sameUser = userRepository.findByEmail(kakaoUserInfoDto.getEmail()).orElse(null);

    if (sameUser == null) {
      return registerKakaoUserIfNeeded(kakaoUserInfoDto);
    } else {
      return updateKakaoUser(sameUser, kakaoUserInfoDto);
    }

  }

  private User updateKakaoUser(User sameUser, KakaoUserInfoDto kakaoUserInfoDto) {

    if (sameUser.getKakaoId() == null) {
      sameUser.setKakaoId(kakaoUserInfoDto.getId());
      userRepository.save(sameUser);
    }
    return sameUser;

  }


  private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {

    Long kakaoId = kakaoUserInfo.getId();
    User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

    if (kakaoUser == null) {

      //같은 이메일의 유저가 없다면
      //신규회원가입
      String nickname = kakaoUserInfo.getNickname();
      String password = UUID.randomUUID().toString();
      String encodedPassword = passwordEncoder.encode(password);

      String email = kakaoUserInfo.getEmail();
      String username = email;

      String userImgUrl = kakaoUserInfo.getUserImgUrl();

//	  TokenDto tokenDto = jwtTokenProvider.createToken(username, username);
//	  RefreshToken refreshToken = RefreshToken.builder()
//		  .key(username)
//		  .value(tokenDto.getRefreshToken())
//		  .build();
//
//	  refreshTokenRepository.save(refreshToken);

      kakaoUser = new User(username, encodedPassword, nickname, email, kakaoId, userImgUrl);

      userRepository.save(kakaoUser);
      Education education = new Education(kakaoUser);
      educationRepository.save(education);


    }

    return kakaoUser;
  }


  private Map<String, Object> forceLogin(User kakaoUser) {
    UserDetails userDetails = new UserDetailsImpl(kakaoUser);
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    TokenDto tokenDto = jwtTokenProvider
        .createToken(kakaoUser.getUsername(), kakaoUser.getUsername());
    List<Long> requestedPostList = getRequestedPostList(kakaoUser);

    Map<String, Object> data = new HashMap<>();
    data.put("userId", kakaoUser.getUserId());
    data.put("nickname", kakaoUser.getNickname());
    data.put("email", kakaoUser.getEmail());
    data.put("userImgUrl", kakaoUser.getUserImgUrl());
    data.put("phone", kakaoUser.getPhoneNumber());
    data.put("token", tokenDto);
    data.put("requestedPostList", requestedPostList);

    RefreshToken refreshToken = RefreshToken.builder()
        .key(kakaoUser.getUsername())
        .value(tokenDto.getRefreshToken())
        .build();

    refreshTokenRepository.save(refreshToken);

    List<Map<String, Object>> eduList = new ArrayList<>();
    Map<String, Object> edu = new HashMap<>();
    Education education = educationRepository.findByUser(kakaoUser).orElse(null);
    edu.put("필수지식", education.getBasic());
    edu.put("심화지식", education.getAdvanced());
    edu.put("심화지식2", education.getCore());
    eduList.add(edu);
    data.put("eduList", eduList);

    return data;
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

}
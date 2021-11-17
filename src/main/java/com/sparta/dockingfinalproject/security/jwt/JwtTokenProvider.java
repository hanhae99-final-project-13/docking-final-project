package com.sparta.dockingfinalproject.security.jwt;

import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {


  private String secretKey = "docking";

  private String secretKeyOfRefreshToken = "accessdocking";

  // 토큰 유효시간
  private Long acessTokenValidTime = 60 * 60 * 1000L;//test30seconds

  private Long refreshTokenValidTime = 60 * 60 * 1000L; //test3분

  private final UserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
	secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  // 토큰 생성
  public TokenDto createToken(String userPk, String username) {

	Claims claims = Jwts.claims().setSubject(userPk);
	claims.put("username", username);

	Date now = new Date();

	String accessToeken = Jwts.builder()
		.setClaims(claims)
		.setIssuedAt(now)
		.setExpiration(new Date(now.getTime() + acessTokenValidTime))
		.signWith(SignatureAlgorithm.HS256, secretKey)
		.compact();

	String refreshToken = Jwts.builder()
//		.setClaims(claims)
		.setIssuedAt(now)
		.setExpiration(new Date(now.getTime() + refreshTokenValidTime))
		.signWith(SignatureAlgorithm.HS256, secretKey)
		.compact();

	return TokenDto.builder()
		.accessToken(accessToeken)
		.accessTokenExpiresIn(acessTokenValidTime)
		.refreshToken(refreshToken)
		.build();


  }


  // 토큰에서 회원 정보 추출
  public String getUserPk(String token) {
	return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  // JWT 토큰에서 인증 정보 조회
  public Authentication getAuthentication(String token) {
	UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
	return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String resolveToken(HttpServletRequest request) {
	return request.getHeader("Authorization");
  }

  // 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String jwtToken) {
	try {
	  Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
	  return !claims.getBody().getExpiration().before(new Date());
	} catch (ExpiredJwtException e) {
	  log.info("만료된 JWT 토큰입니다.");
	  return false;
	}
  }

  public String getAccessTokenPayload(String accessToken) {

	return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken)
		.getBody().getSubject();
  }

}
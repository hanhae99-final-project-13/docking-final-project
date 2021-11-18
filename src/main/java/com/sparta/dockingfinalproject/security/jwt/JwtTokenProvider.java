package com.sparta.dockingfinalproject.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {


  private String secretKey = "docking";

  private Long acessTokenValidTime = 30 * 60 * 1000L;//30분

  private Long refreshTokenValidTime = 14 * 24 * 60 * 60 * 1000L;//2주

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
	  log.info(e.getMessage());
	} catch (UnsupportedJwtException e) {
	  log.info("지원되지 않는 JWT 토큰입니다.");
	  log.info(e.getMessage());
	} catch (IllegalArgumentException e) {
	  log.info("JWT 토큰이 잘못되었습니다.");
	  log.info(e.getMessage());
	} catch (MalformedJwtException e) {
	  log.info("잘못된 JWT 서명입니다.");
	  log.info(e.getMessage());
	} catch (Exception e) {
	  log.info(e.getMessage());
	}
	return false;
  }

  public String getAccessTokenPayload(String accessToken) {

	return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken)
		.getBody().getSubject();
  }

}
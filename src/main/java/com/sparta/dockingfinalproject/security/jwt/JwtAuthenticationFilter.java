package com.sparta.dockingfinalproject.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	  throws IOException, ServletException {
	// 헤더에서 jwt 토큰 받아옴
	//api 호출 시 헤더의 accessToken을 확인하고 유효한지, 만료기간이 지났는지 체크

	String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
	System.out.println(token);
	if (token != null && !token.isEmpty()) {
	  token = token.replaceAll("Bearer", "");
	}
	System.out.println(token);

	if (token != null && jwtTokenProvider.validateToken(token)) {
	  Authentication authentication = jwtTokenProvider.getAuthentication(token);
	  SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	chain.doFilter(request, response);

  }
}
package com.sparta.dockingfinalproject.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.KakaoUserInfoDto;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
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

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    //회원 등록
    public void registerUser(SignupRequestDto requestDto) {



       String username = requestDto.getUsername();
       Optional<User> found = userRepository.findByUsername(username);
       if (found.isPresent()){
           throw new IllegalArgumentException("중복된 이메일 존재합니다");
       }

       String password = requestDto.getPassword();
       String pwcheck = requestDto.getPwcheck();

       if(!password.equals(pwcheck)) {
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
       }
       //패스워드 인코딩 완료
       password = passwordEncoder.encode(password);

       String nickname = requestDto.getNickname();
       String email = requestDto.getEmail();

       User user = new User(username, password, nickname, email);

       userRepository.save(user);

    }

    public User login (UserRequestDto requestDto){

        User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);


        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 불일치 합니다");
        }
       return user;
    }




}



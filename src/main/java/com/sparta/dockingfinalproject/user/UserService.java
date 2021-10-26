package com.sparta.dockingfinalproject.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
       password = passwordEncoder.encode(password);
       String nickname = requestDto.getNickname();


    }
}

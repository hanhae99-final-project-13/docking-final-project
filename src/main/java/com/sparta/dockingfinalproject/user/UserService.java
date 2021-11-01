package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
       String password = requestDto.getPassword();
       String pwcheck = requestDto.getPwcheck();
       String userImgUrl = "이미지url";


       if(!password.equals(pwcheck)) {
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
       }
       //패스워드 인코딩 완료
       password = passwordEncoder.encode(password);

       String nickname = requestDto.getNickname();
       String email = requestDto.getEmail();
//       String userImgUrl = requestDto.getUserImgUrl();


         User user = new User(username, password, nickname, email, userImgUrl);

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



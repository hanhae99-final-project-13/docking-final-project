package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import javax.transaction.Transactional;
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
    public void registerUser(SignupRequestDto requestDto, String authKey) {

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


         User user = new User(username, password, nickname, email, userImgUrl, authKey);

         userRepository.save(user);

    }

    public User login (UserRequestDto requestDto){

        User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);


        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 불일치 합니다");
        }
       return user;
    }


    @Transactional
    public void singUpConfirm(String email, String authKey) throws Exception {
      User user = userRepository.findByEmail(email).orElseThrow(
          () -> new IllegalArgumentException("인증번호가 만료되었습니다. 다시 회원가입 해주세요")
      );

      if(user.getAuthKey().equalsIgnoreCase(authKey)){
        user.confirm();
      } else {
        throw new DockingException(ErrorCode.POST_NOT_FOUND);
      }


    }
}



package com.sparta.dockingfinalproject.user;


import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
//      userRepository.findAllByAuth

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

    public User login (SignupRequestDto requestDto){

        User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);
//      User user = userRepository.findAllByAuthCheckTrueAndUsername(requestDto.getUsername()).orElseThrow(
//          () -> new IllegalArgumentException("이메일 인증 부터 해주세요")
//      );


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

@Transactional
  public Map<String, Object> updateUser(UserDetailsImpl userDetails, SignupRequestDto requestDto) {

      User findUser = userRepository.findById(userDetails.getUser().getUserId()).orElseThrow(
          () -> new DockingException(ErrorCode.USER_NOT_FOUND)
      );

      findUser.update(requestDto);
    //리턴 data 생성
    Map<String, String> data = new HashMap<>();
    data.put("msg", "사용자 정보가 수정 되었습니다");
    return SuccessResult.success(data);
  }
}



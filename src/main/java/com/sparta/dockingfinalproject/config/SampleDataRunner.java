package com.sparta.dockingfinalproject.config;

import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetRepository;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import com.sparta.dockingfinalproject.user.UserService;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleDataRunner implements ApplicationRunner {

  private final PetRepository petRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final UserService userService;

  UserRequestDto userRequestDto = new UserRequestDto();
  String username = "";
  User user = new User();
  ArrayList<Pet> apiPetList = new ArrayList<>();

  public void run(ApplicationArguments args) throws Exception {
    Pet samplePet = createSamplePet();

    sampleUserSignup(11);
    sampleUserLogin(11);
    username = userRequestDto.getUsername();
    user = userRepository.findByUsername(username).orElseThrow(
        () -> new IllegalArgumentException("유저가 존재하지 않습니다"));

    Post post = new Post(samplePet, user);
    postRepository.save(post);

    sampleUserSignup(22);
    sampleUserSignup(33);
    sampleUserSignup(44);
  }

  private Pet createSamplePet() {
    Pet samplePet = Pet.builder()
        .breed("부르부르도그")
        .sex("F")
        .age("2021")
        .weight("2")
        .lostLocation("호수공원")
        .ownerType("개인")
        .address("서울시 광진구")
        .phone("01077773333")
        .tag("직접등록")
        .img(
            "https://i.pinimg.com/236x/9f/73/74/9f7374548b9bd505f5229fa7fc81d8d6--illustration-art-kawaii.jpg")
        .extra("부들부들떨어요 빨리 데려가주세요")
        .isAdopted("abandoned")
        .petNo("")
        .build();

    apiPetList.add(samplePet);
    petRepository.save(samplePet);
    return samplePet;
  }

  private void sampleUserSignup(int num) {
    SignupRequestDto signupRequestDto = new SignupRequestDto();
    signupRequestDto.setUsername("sampleuser" + num);
    signupRequestDto.setPassword("sample00");
    signupRequestDto.setPwcheck("sample00");
    signupRequestDto.setNickname("샘플유저" + num);
    signupRequestDto.setEmail("sampleuser"+num+"@sparta.com");
    signupRequestDto.setPhoneNumber("01088881111");

    userService.registerUser(signupRequestDto);
  }

  private void sampleUserLogin(int num) {
    userRequestDto.setUsername("sampleuser" + num);
    userRequestDto.setPassword("sample1023");
  }

}

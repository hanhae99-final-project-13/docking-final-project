package com.sparta.dockingfinalproject.user;

import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UpdateRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String email;

  @Column
  private String badge;

  //추가부분
  @Column(length = 1500)
  private String userImgUrl;

  @Column(unique = true)
  private Long kakaoId;


  @Column(nullable = false)
  private String phoneNumber;

  public User(String username, String password, String nickname, String email, String userImgUrl,
	  String phoneNumber) {

	this.username = username;
	this.password = password;
	this.nickname = nickname;
	this.email = email;
	this.userImgUrl = userImgUrl;
	this.kakaoId = null;
	this.phoneNumber = phoneNumber;


  }

  public User(String username, String password, String nickname, String email, Long kakaoId,
	  String userImgUrl) {
	this.username = username;
	this.password = password;
	this.nickname = nickname;
	this.email = email;
	this.kakaoId = kakaoId;
	this.userImgUrl = userImgUrl;
	this.phoneNumber = "";

  }


  public User(String password, String nickname, String email, Long kakaoId) {
	this.password = password;
	this.nickname = nickname;
	this.email = email;
	this.kakaoId = kakaoId;

  }

  public User(SignupRequestDto requestDto, String password) {
	this.username = requestDto.getUsername();
	this.password = password;
	this.nickname = requestDto.getNickname();
	this.email = requestDto.getEmail();
	this.userImgUrl = "https://gorokke.shop/image/profileDefaultImg.jpg";
	this.kakaoId = null;
	this.phoneNumber = requestDto.getPhoneNumber();

  }

  public void update(UpdateRequestDto requestDto) {
	this.nickname = requestDto.getNickname();
	this.userImgUrl = requestDto.getUserImgUrl();

  }

  public boolean validateUser(Long userId) {
    return this.userId.equals(userId);
  }

}

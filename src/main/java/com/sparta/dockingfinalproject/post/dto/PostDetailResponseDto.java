package com.sparta.dockingfinalproject.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Sex;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PostDetailResponseDto {
  private Long userId;
  private String nickname;
  private Long postId;
  private String breed;
  private Sex sex;
  private int age;
  private double weight;
  private String lostLocation;
  private String ownerType;
  private String phone;
  private String address;
  private String tag;
  private String url;
  private List<String> img = new ArrayList<>();
  private String extra;
  private IsAdopted isAdopted;
  private boolean heart;

  @QueryProjection
  public PostDetailResponseDto(Long userId, String nickname, Long postId, String breed, Sex sex, int age, double weight,
      String lostLocation, String ownerType, String phone, String address, String tag, String url, String imgs, String extra,
      IsAdopted isAdopted) {
    this.userId = userId;
    this.nickname = nickname;
    this.postId = postId;
    this.breed = breed;
    this.sex = sex;
    this.age = age;
    this.weight = weight;
    this.lostLocation = lostLocation;
    this.ownerType = ownerType;
    this.phone = phone;
    this.address = address;
    this.tag = tag;
    this.url = url;
    this.extra = extra;
    this.isAdopted = isAdopted;
    this.heart = false;

    String[] str = imgs.split(" ## ");
    for (String x : str) {
      this.img.add(x);
    }
  }

  public void addHeart(boolean heart) {
    this.heart = heart;
  }
}

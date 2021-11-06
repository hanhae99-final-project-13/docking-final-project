package com.sparta.dockingfinalproject.post.dto;

import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.Post;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
  private List<String> img;
  private String isAdopted;
  private boolean heart;

  public static PostDetailResponseDto getPostDetailResponseDto(Post post, boolean heart) {
    Pet pet = post.getPet();
    return PostDetailResponseDto.builder()
            .userId(post.getUser().getUserId())
            .nickname(post.getUser().getNickname())
            .postId(post.getPostId())
            .breed(pet.getBreed())
            .sex(pet.getSex())
            .weight(pet.getWeight())
            .lostLocation(pet.getLostLocation())
            .ownerType(pet.getOwnerType())
            .phone(pet.getPhone())
            .address(pet.getAddress())
            .tag(pet.getTag())
            .url(pet.getUrl())
            .img(getImgs(pet.getImg()))
            .isAdopted(pet.getIsAdopted())
            .heart(heart)
            .build();
  }

  public static PostDetailResponseDto getPostDetailResponseDto(Post post) {
    Pet pet = post.getPet();

    return PostDetailResponseDto.builder()
        .userId(post.getUser().getUserId())
        .nickname(post.getUser().getNickname())
        .postId(post.getPostId())
        .breed(pet.getBreed())
        .sex(pet.getSex())
        .weight(pet.getWeight())
        .lostLocation(pet.getLostLocation())
        .ownerType(pet.getOwnerType())
        .phone(pet.getPhone())
        .tag(pet.getTag())
        .url(pet.getUrl())
        .img(getImgs(pet.getImg()))
        .isAdopted(pet.getIsAdopted())
        .build();
  }

  private static List<String> getImgs(String data) {
    List<String> imgs = new ArrayList<>();
    String[] str = data.split(" ## ");
    for (String x : str) {
      imgs.add(x);
    }
    return imgs;
  }
}

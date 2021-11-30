package com.sparta.dockingfinalproject.pet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.dockingfinalproject.common.Timestamped;
import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.model.Post;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long petId;

  @Column(nullable = false)
  private String breed;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Sex sex;

  @Column(nullable = false)
  private int age;

  @Column(nullable = false)
  private double weight;

  @Column(nullable = false)
  private String lostLocation;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String phone;

  @Column
  private String tag;

  @Column
  private String url;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private IsAdopted isAdopted;

  @Column(nullable = false)
  private String ownerType;

  @Column
  private String petNo;

  @Column(nullable = false, length = 3000)
  private String extra;

  @Column(nullable = false, length = 1500)
  private String img;

  @OneToOne(mappedBy = "pet")
  @JsonBackReference
  private Post post;

  @Builder
  public Pet(String breed, String sex, String age, String weight, String lostLocation,
      String ownerType,
      String address,String phone, String tag, String img, String extra, String isAdopted, String petNo) {
    this.breed = breed;
    this.sex = Sex.of(sex);
    this.age = Integer.parseInt(age.replaceAll("[^0-9]", ""));
    this.weight = Double.parseDouble(weight.replace("..", ".").replaceAll("[^0-9.]", ""));
    this.lostLocation = lostLocation;
    this.ownerType = ownerType;
    this.address = address;
    this.phone = phone;
    this.tag = tag;
    this.img = img;
    this.extra = extra;
    this.isAdopted = IsAdopted.of(isAdopted);
    this.petNo = petNo;
  }

  public Pet(PetRequestDto petRequestDto) {
    this.breed = petRequestDto.getBreed();
    this.sex = Sex.of(petRequestDto.getSex());
    this.age = petRequestDto.getAge();
    this.weight = petRequestDto.getWeight();
    this.lostLocation = petRequestDto.getLostLocation();
    this.ownerType = petRequestDto.getOwnerType();
    this.address = petRequestDto.getAddress();
    this.phone = petRequestDto.getPhone();
    this.tag = petRequestDto.getTag();
    this.url = petRequestDto.getUrl();

    List<String> imgs = petRequestDto.getImg();
    String temp = "";
    for (String im : imgs) {
      temp += (im + " ## ");
    }
    this.img = temp;

    this.extra = petRequestDto.getExtra();
    this.isAdopted = IsAdopted.of(petRequestDto.getIsAdopted());
  }

  public void addPost(Post post) {
    this.post = post;
  }

  public Pet updateStatus(String isAdopted) {
    this.isAdopted = IsAdopted.of(isAdopted);
    return this;
  }

  public Pet update(PetRequestDto petRequestDto) {
    if (petCheck(petRequestDto.getBreed())) {
      this.breed = petRequestDto.getBreed();
    }
    this.age = petRequestDto.getAge();
    this.weight = petRequestDto.getWeight();

    if (petCheck(petRequestDto.getLostLocation())) {
      this.lostLocation = petRequestDto.getLostLocation();
    }

    if (petCheck(petRequestDto.getOwnerType())) {
      this.ownerType = petRequestDto.getOwnerType();
    }

    if (petCheck(petRequestDto.getAddress())) {
      this.address = petRequestDto.getAddress();
    }

    if (petCheck(petRequestDto.getPhone())) {
      this.phone = petRequestDto.getPhone();
    }

    if (petCheck(petRequestDto.getTag())) {
      this.tag = petRequestDto.getTag();
    }

    if (petCheck(petRequestDto.getUrl())) {
      this.url = petRequestDto.getUrl();
    }

    if (petRequestDto.getImg().size() > 0) {
      List<String> imgs = petRequestDto.getImg();
      String temp = "";
      for (String img : imgs) {
        temp += img + " ## ";
      }
      this.img = temp;
    }

    if (petCheck(petRequestDto.getExtra())) {
      this.extra = petRequestDto.getExtra();
    }

    if (petCheck(petRequestDto.getIsAdopted())) {
      this.isAdopted = IsAdopted.of(petRequestDto.getIsAdopted());
    }

    if (petRequestDto.getSex() != null) {
      this.sex = Sex.of(petRequestDto.getSex());
    }

    return this;
  }

  private boolean petCheck(String data) {
    return data != null && !data.isEmpty();
  }
}
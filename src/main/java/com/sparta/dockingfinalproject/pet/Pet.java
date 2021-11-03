package com.sparta.dockingfinalproject.pet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.dockingfinalproject.common.Timestamped;
import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.Post;
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

  @Column(nullable = false)
  private String isAdopted;

  @Column(nullable = false)
  private String ownerType;

  @Column
  private String petNo;

  @Column(nullable = false)
  private String extra;

  @Column(nullable = false)
  private String img;

  @OneToOne(mappedBy = "pet")
  @JsonBackReference
  private Post post;

  @Builder
  public Pet(String breed, String sex, String age, String weight, String lostLocation,
      String ownerType,
      String address, String phone, String img, String extra, String isAdopted, String petNo) {
    this.breed = breed;
    this.sex = Sex.of(sex);
    this.age = Integer.parseInt(age.replaceAll("[^0-9]", ""));
    this.weight = Double.parseDouble(weight.replaceAll("[^0-9.]", ""));
    this.lostLocation = lostLocation;
    this.ownerType = ownerType;
    this.address = address;
    this.phone = phone;
    this.img = img;
    this.extra = extra;
    this.isAdopted = isAdopted;
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
    this.img = petRequestDto.getImg();
    this.extra = petRequestDto.getExtra();
    this.isAdopted = petRequestDto.getIsAdopted();
  }

  public void addPost(Post post) {
    this.post = post;
  }

  public void updateStatus(String isAdopted) {
    this.isAdopted = isAdopted;
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

    if (petCheck(petRequestDto.getImg())) {
      this.img = petRequestDto.getImg();
    }

    if (petCheck(petRequestDto.getExtra())) {
      this.extra = petRequestDto.getExtra();
    }

    if (petCheck(petRequestDto.getIsAdopted())) {
      this.isAdopted = petRequestDto.getIsAdopted();
    }

    return this;
  }

  private boolean petCheck(String data) {
    return data != null && !data.isEmpty();
  }
}

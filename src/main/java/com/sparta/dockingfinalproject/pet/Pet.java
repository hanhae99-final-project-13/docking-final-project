package com.sparta.dockingfinalproject.pet;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Pet {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long petId;

  @Column(nullable = false)
  private String petName;

  @Column(nullable = false)
  private String breed;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private PetSex sex;

  @Column(nullable = false)
  private int age;

  @Column(nullable = false)
  private double weight;

  @Column(nullable = false)
  private String lostLocation;

  @Column(nullable = false)
  private String ownerType;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String tag;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private String img;

  @Column(nullable = false)
  private String extra;

  @Column(nullable = false)
  private String isAdopted;

  @OneToOne(mappedBy = "pet")
  private Post post;

  public Pet(PetRequestDto petRequestDto) {
    this.petName = petRequestDto.getPetName();
    this.breed = petRequestDto.getBreed();
    this.sex = PetSex.of(petRequestDto.getSex());
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
}

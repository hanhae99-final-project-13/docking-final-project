package com.sparta.dockingfinalproject.pet;

import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.Post;
import javax.persistence.Column;
import javax.persistence.Entity;
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
  private String sex;

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

  public Pet(String petName, String breed, String sex, int age)

  public Pet(PetRequestDto petRequestDto) {

  }

  public void addPost(Post post) {
    this.post = post;
  }
}

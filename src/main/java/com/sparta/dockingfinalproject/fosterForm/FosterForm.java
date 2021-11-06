package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FosterForm {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long FosterFormId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Long fosterAge;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Sex gender;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String job;

  @Column(nullable = false)
  private String fosterAddress;

  @Column(nullable = false)
  private String currentPet;

  @Column(nullable = false)
  private String experience;

  @Column(nullable = false)
  private String reason;

  @Column(nullable = false)
  private String allergy;

  @Column(nullable = false)
  private String family;

  @Column(nullable = false)
  private String timeTogether;

  @Column(nullable = false)
  private String anxiety;

  @Column(nullable = false)
  private String bark;

  @Column(nullable = false)
  private String roomUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "POST_ID", nullable = false)
  private Post post;


  public FosterForm(Post post, FosterFormRequestDto FosterFormRequestDto, User user) {
    this.name = FosterFormRequestDto.getName();
    this.fosterAge = FosterFormRequestDto.getFosterAge();
    this.gender = FosterFormRequestDto.getGender();
    this.phone = FosterFormRequestDto.getPhone();
    this.job = FosterFormRequestDto.getJob();
    this.fosterAddress = FosterFormRequestDto.getFosterAddress();
    this.currentPet = FosterFormRequestDto.getCurrentPet();
    this.experience = FosterFormRequestDto.getExperience();
    this.reason = FosterFormRequestDto.getReason();
    this.allergy = FosterFormRequestDto.getAllergy();
    this.family = FosterFormRequestDto.getFamily();
    this.timeTogether = FosterFormRequestDto.getTimeTogether();
    this.anxiety = FosterFormRequestDto.getAnxiety();
    this.bark = FosterFormRequestDto.getBark();
    this.roomUrl = FosterFormRequestDto.getRoomUrl();
    this.user = user;
    this.post = post;
  }

}
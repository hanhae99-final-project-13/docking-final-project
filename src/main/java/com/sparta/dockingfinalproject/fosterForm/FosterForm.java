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


  public FosterForm(Post post, FosterFormRequestDto fosterFormrRequestDto, User user) {
    this.name = fosterFormrRequestDto.getName();
    this.fosterAge = fosterFormrRequestDto.getFosterAge();
    this.gender = fosterFormrRequestDto.getGender();
    this.phone = fosterFormrRequestDto.getPhone();
    this.job = fosterFormrRequestDto.getJob();
    this.fosterAddress = fosterFormrRequestDto.getFosterAddress();
    this.currentPet = fosterFormrRequestDto.getCurrentPet();
    this.experience = fosterFormrRequestDto.getExperience();
    this.reason = fosterFormrRequestDto.getReason();
    this.allergy = fosterFormrRequestDto.getAllergy();
    this.family = fosterFormrRequestDto.getFamily();
    this.timeTogether = fosterFormrRequestDto.getTimeTogether();
    this.anxiety = fosterFormrRequestDto.getAnxiety();
    this.bark = fosterFormrRequestDto.getBark();
    this.roomUrl = fosterFormrRequestDto.getRoomUrl();
    this.user = user;
    this.post = post;
  }

}
package com.sparta.dockingfinalproject.fosterForm.model;

import com.sparta.dockingfinalproject.common.Timestamped;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.model.Post;
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
public class FosterForm extends Timestamped {

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
  private String leaveHome;

  @Column(nullable = false)
  private String medicalBudget;

  @Column(nullable = false)
  private String monthlyBudget;

  @Column(nullable = false)
  private String roomUrl;

  @Column(nullable = false)
  private String signUrl;

  @Column(length = 3000)
  private String etc;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Acceptance acceptance;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "POST_ID", nullable = false)
  private Post post;

  public FosterForm(Post post, FosterFormRequestDto fosterFormRequestDto, User user,
      Acceptance acceptance) {
    this.name = fosterFormRequestDto.getName();
    this.fosterAge = fosterFormRequestDto.getFosterAge();
    this.gender = Sex.of(fosterFormRequestDto.getGender());
    this.phone = fosterFormRequestDto.getPhone();
    this.job = fosterFormRequestDto.getJob();
    this.fosterAddress = fosterFormRequestDto.getFosterAddress();
    this.currentPet = fosterFormRequestDto.getCurrentPet();
    this.experience = fosterFormRequestDto.getExperience();
    this.reason = fosterFormRequestDto.getReason();
    this.allergy = fosterFormRequestDto.getAllergy();
    this.family = fosterFormRequestDto.getFamily();
    this.timeTogether = fosterFormRequestDto.getTimeTogether();
    this.anxiety = fosterFormRequestDto.getAnxiety();
    this.bark = fosterFormRequestDto.getBark();
    this.leaveHome = fosterFormRequestDto.getLeaveHome();
    this.medicalBudget = fosterFormRequestDto.getMedicalBudget();
    this.monthlyBudget = fosterFormRequestDto.getMonthlyBudget();
    this.roomUrl = fosterFormRequestDto.getRoomUrl();
    this.signUrl = fosterFormRequestDto.getSignUrl();
    this.etc = fosterFormRequestDto.getEtc();
    this.acceptance = acceptance;
    this.user = user;
    this.post = post;
  }

  public void updateAcceptance(Acceptance acceptance) {
    this.acceptance = acceptance;
  }

}
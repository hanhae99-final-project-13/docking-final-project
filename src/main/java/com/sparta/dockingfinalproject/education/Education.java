package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
public class Education {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eduFinishId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;


  @Column
  private String edu;

  @Column(nullable = false)
  private boolean eduDone;


  public Education(User user, String edu){
    this.user = user;
    this.edu = edu;
    this.eduDone = true;


  }


}

package com.sparta.dockingfinalproject.alarm.model;


import com.sparta.dockingfinalproject.common.Timestamped;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long alarmId;

  @Column(nullable = false)
  private String alarmContent;

  @Column(nullable = false)
  private boolean checked;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private AlarmType alarmType;

  @Column
  private Long contentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;


  public Alarm(String alarmContent, AlarmType alarmType, Long contentId, User user) {
    this.alarmContent = alarmContent;
    this.checked = true;
    this.alarmType = alarmType;
    this.contentId = contentId;
    this.user = user;
  }

  public void updateIsRead() {
    this.checked = false;
  }
}
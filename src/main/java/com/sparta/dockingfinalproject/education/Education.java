package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.user.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Education {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eduId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @Column
  private Boolean basic;

  @Column
  private Boolean advanced;

  @Column
  private Boolean core;

public Education(User user){
  this.user = user;
  this.basic = false;
  this.advanced = false;
  this.core = false;
}

}

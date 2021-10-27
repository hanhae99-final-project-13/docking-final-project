package com.sparta.dockingfinalproject.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.dockingfinalproject.comment.Comment;
import com.sparta.dockingfinalproject.common.Timestamped;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.wish.Wish;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long postId;

  @Column(nullable = false)
  private Long viewCount;

  @OneToOne
  @JoinColumn(name = "PET_ID", nullable = false)
  private Pet pet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

//  @OneToMany(mappedBy = "post", orphanRemoval = true)
//  @JsonIgnore
//  private List<Comment> commentList;

  @OneToMany(mappedBy = "post", orphanRemoval = true)
  @JsonIgnore
  private List<Wish> wishList;
}

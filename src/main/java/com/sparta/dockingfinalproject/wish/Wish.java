package com.sparta.dockingfinalproject.wish;

import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.user.User;
import javax.persistence.Entity;
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
public class Wish {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long wishId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "POST_ID", nullable = false)
  private Post post;

  public void addUser(User user) {
    this.user = user;
  }

  public void addPost(Post post) {
    if (this.post != null) {
      this.post.getWishList().remove(this);
    }

    this.post = post;
    post.addWish(this);
  }
}

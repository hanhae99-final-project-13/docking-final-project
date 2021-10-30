package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.common.Timestamped;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long commentId;

  @Column(nullable = false)
  private String comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "POST_ID", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  public Comment(Post post, CommentRequestDto commentRequestDto, User user) {
    this.comment = commentRequestDto.getComment();
    this.post = post;
    this.user = user;
  }

  public Comment update(CommentRequestDto commentRequestDto) {
    if (commentCheck(commentRequestDto.getComment())) {
      this.comment = commentRequestDto.getComment();
    }
    return this;
  }

  private boolean commentCheck(String data) {
    return data != null && !data.isEmpty();
  }

}
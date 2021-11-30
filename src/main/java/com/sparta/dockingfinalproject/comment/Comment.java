package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentEditRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.common.Timestamped;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

  public Comment update(CommentEditRequestDto requestDto) {
    if (commentCheck(requestDto.getComment())) {
      this.comment = requestDto.getComment();
    }
    return this;
  }

  private boolean commentCheck(String data) {
    return data != null && !data.isEmpty();
  }

}
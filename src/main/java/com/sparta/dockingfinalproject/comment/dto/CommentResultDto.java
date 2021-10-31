package com.sparta.dockingfinalproject.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResultDto {

  private Long commentId;
  private String comment;
  private String nickname;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public CommentResultDto(Long commentId, String comment, String nickname, LocalDateTime createdAt,
      LocalDateTime modifiedAt) {
    this.commentId = commentId;
    this.comment = comment;
    this.nickname = nickname;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }
}

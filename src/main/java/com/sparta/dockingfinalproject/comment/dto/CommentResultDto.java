package com.sparta.dockingfinalproject.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.dockingfinalproject.comment.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResultDto {

  private Long commentId;
  private String comment;
  private String nickname;
  private String userImgUrl;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  @QueryProjection
  public CommentResultDto(Long commentId, String comment, String nickname, String userImgUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.commentId = commentId;
    this.comment = comment;
    this.nickname = nickname;
    this.userImgUrl = userImgUrl;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }

  public static CommentResultDto of(Comment comment) {
    return new CommentResultDto(comment.getCommentId(), comment.getComment(), comment.getUser().getNickname(),
        comment.getUser().getUserImgUrl(), comment.getCreatedAt(), comment.getModifiedAt());
  }
}

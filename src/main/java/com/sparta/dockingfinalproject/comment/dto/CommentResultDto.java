package com.sparta.dockingfinalproject.comment.dto;

import com.sparta.dockingfinalproject.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResultDto {

  private Long commentId;
  private String comment;
  private String nickname;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static CommentResultDto of(Comment comment) {
    return CommentResultDto.builder()
        .commentId(comment.getCommentId())
        .comment(comment.getComment())
        .nickname(comment.getUser().getNickname())
        .createdAt(comment.getCreatedAt())
        .modifiedAt(comment.getModifiedAt())
        .build();
  }
}

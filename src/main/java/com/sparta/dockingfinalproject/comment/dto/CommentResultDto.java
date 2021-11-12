package com.sparta.dockingfinalproject.comment.dto;

import com.sparta.dockingfinalproject.comment.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentResultDto {

  private Long commentId;
  private String comment;
  private String nickname;
  private String userImgUrl;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static CommentResultDto of(Comment comment) {
    return CommentResultDto.builder()
        .commentId(comment.getCommentId())
        .comment(comment.getComment())
        .nickname(comment.getUser().getNickname())
        .userImgUrl(comment.getUser().getUserImgUrl())
        .createdAt(comment.getCreatedAt())
        .modifiedAt(comment.getModifiedAt())
        .build();
  }
}

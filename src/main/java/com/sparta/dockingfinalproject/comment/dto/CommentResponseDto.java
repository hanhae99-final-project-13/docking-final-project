package com.sparta.dockingfinalproject.comment.dto;

import com.sparta.dockingfinalproject.user.User;

import java.time.LocalDateTime;

public interface CommentResponseDto {

  String getComment();

  Long getCommentId();

  User getUser();

  LocalDateTime getCreatedAt();

  LocalDateTime getModifiedAt();
}

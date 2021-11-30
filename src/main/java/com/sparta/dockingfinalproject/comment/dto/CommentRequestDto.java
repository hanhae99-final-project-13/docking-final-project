package com.sparta.dockingfinalproject.comment.dto;

import com.sparta.dockingfinalproject.exception.ErrorMessage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

  @NotNull(message = ErrorMessage.POST_ID_REQUIRED)
  private Long postId;

  @NotBlank(message = ErrorMessage.COMMENT_REQUIRED)
  private String comment;
}

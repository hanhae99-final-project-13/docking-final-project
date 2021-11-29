package com.sparta.dockingfinalproject.comment.dto;

import com.sparta.dockingfinalproject.exception.ErrorMessage;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEditRequestDto {

  @NotBlank(message = ErrorMessage.COMMENT_REQUIRED)
  private String comment;
}

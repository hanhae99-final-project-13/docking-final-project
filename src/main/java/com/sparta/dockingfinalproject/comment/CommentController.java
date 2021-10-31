package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;

  // Comment 등록
  @PostMapping("/comments/{postId}")
  public Map<String, Object> addComment(@PathVariable Long postId,
      @RequestBody CommentRequestDto commentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.addComment(postId, commentRequestDto, userDetails);
  }

  // Comment 수정
  @PatchMapping("/comments/{commentId}")
  public Map<String, Object> updateComment(@PathVariable Long commentId,
      @RequestBody CommentRequestDto commentRequestDto) {
    return commentService.updateComment(commentId, commentRequestDto);
  }

  // Comment 삭제
  @DeleteMapping("/comments/{commentId}")
  public Map<String, Object> deleteComment(@PathVariable Long commentId) {
    return commentService.deleteComment(commentId);
  }
}

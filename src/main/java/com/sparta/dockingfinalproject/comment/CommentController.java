package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentEditRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/comments")
  public ResponseEntity<Map<String, Object>> addComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(commentService.addComment(commentRequestDto, userDetails));
  }

  @PatchMapping("/comments/{commentId}")
  public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long commentId,
      @Valid @RequestBody CommentEditRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(commentService.updateComment(commentId, requestDto, userDetails));
  }

  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(commentService.deleteComment(commentId, userDetails));
  }
}

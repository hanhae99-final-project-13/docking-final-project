package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.dto.StatusDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  // 게시글 상세 조회
  @GetMapping("/posts/{postId}")
  public Map<String, Object> getPost(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.getPost(postId, userDetails);
  }

  // 게시글 등록
  @PostMapping("/posts")
  public Map<String, Object> addPost(@RequestBody PetRequestDto petRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.addPost(petRequestDto, userDetails);
  }

  // 게시글 수정
  @PatchMapping("/posts/{postId}")
  public Map<String, Object> updatePost(@PathVariable Long postId,
      @RequestBody PetRequestDto petRequestDto) {
    return postService.updatePost(postId, petRequestDto);
  }

  // 게시글 삭제
  @DeleteMapping("/posts/{postId}")
  public Map<String, Object> deletePost(@PathVariable Long postId) {
    return postService.deletePost(postId);
  }

  // 보호상태 변경
  @PatchMapping("/{postId}/completions")
  public Map<String, Object> updateStatus(@PathVariable Long postId,
      @RequestBody StatusDto statusDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.updateStatus(postId, statusDto, userDetails);
  }
}

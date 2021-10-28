package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/posts/{postId}")
  public Map<String, Object> getPosts(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.getPosts(postId, userDetails);
  }

  // 게시글 등록
  @PostMapping("/pets")
  public Map<String, Object> addPost(@RequestBody PetRequestDto petRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.addPost(petRequestDto, userDetails);
  }

  // 게시글 수정
  @PostMapping("/posts/{postId}")
  public Map<String, Object> updatePost(@PathVariable Long postId, @RequestBody PetRequestDto petRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.updatePost(postId, petRequestDto, userDetails);
  }
}

package com.sparta.dockingfinalproject.post;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/posts/{postId}")
  public Map<String, Object> getPosts(@PathVariable Long postId) {
    postService.getPosts(Long postId);
  }
}

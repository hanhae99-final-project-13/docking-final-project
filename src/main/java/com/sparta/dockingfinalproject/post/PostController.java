package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
  public Map<String, Object> getPosts(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
    return postService.getPosts(postId, userDetails);
  }

  @PostMapping("/pets")
  public Map<String, Object> addPost(@RequestBody PetRequestDto petRequestDto, @AuthenticationPrincipal UserDetails userDetails) {
    return postService.addPost(petRequestDto, userDetails);
  }
}

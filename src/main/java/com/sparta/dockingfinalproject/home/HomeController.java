package com.sparta.dockingfinalproject.home;

import com.sparta.dockingfinalproject.post.PostService;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  private final PostService postService;

  public HomeController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/posts")
  public ResponseEntity<Map<String, Object>> home(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.home(userDetails));
  }
}

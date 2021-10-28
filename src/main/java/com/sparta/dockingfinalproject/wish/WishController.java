package com.sparta.dockingfinalproject.wish;

import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WishController {

  private final WishService wishService;

  public WishController(WishService wishService) {
    this.wishService = wishService;
  }

  @PostMapping("/wishes/{postId}")
  public Map<String, Object> addWish(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return wishService.addWish(postId, userDetails);
  }
}

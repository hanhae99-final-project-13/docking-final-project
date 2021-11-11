package com.sparta.dockingfinalproject.wish;

import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.wish.dto.WishRequestDto;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WishController {

  private final WishService wishService;

  public WishController(WishService wishService) {
    this.wishService = wishService;
  }

  @PostMapping("/wishes")
  public Map<String, Object> addWish(@RequestBody WishRequestDto wishRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return wishService.addWishAndDeleteWish(wishRequestDto.getPostId(), userDetails);
  }

  @GetMapping("/user/wishes")
  public Map<String, Object> getWishes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return wishService.getWishes(userDetails.getUser());
  }
}

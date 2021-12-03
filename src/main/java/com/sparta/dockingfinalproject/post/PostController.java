package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.post.dto.request.PostSearchRequestDto;
import com.sparta.dockingfinalproject.post.dto.request.StatusDto;
import com.sparta.dockingfinalproject.post.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  // 게시글 상세 조회
  @GetMapping("/posts/{postId}")
  public ResponseEntity<Map<String, Object>> getPost(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.getPost(postId, userDetails));
  }

  // 게시글 등록
  @PostMapping("/posts")
  public ResponseEntity<Map<String, Object>> addPost(@RequestBody PetRequestDto petRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.addPost(petRequestDto, userDetails));
  }

  // 게시글 수정
  @PatchMapping("/posts/{postId}")
  public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long postId,
      @RequestBody PetRequestDto petRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.updatePost(postId, petRequestDto, userDetails));
  }

  // 게시글 삭제
  @DeleteMapping("/posts/{postId}")
  public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.deletePost(postId, userDetails));
  }

  // 보호상태 변경
  @PatchMapping("/posts/{postId}/completions")
  public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable Long postId,
      @RequestBody StatusDto statusDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.updateStatus(postId, statusDto, userDetails));
  }

  // 입양페이지 조건 검색 조회
  @GetMapping("/posts/search/{page}")
  public ResponseEntity<Map<String, Object>> getPostsTermsSearch(@PathVariable int page, @RequestParam(required = false) String startDt,
      @RequestParam(required = false) String endDt, @RequestParam(required = false) String ownerType,
      @RequestParam(required = false) String city, @RequestParam(required = false) String district,
      @RequestParam(required = false) String sort) {

    PostSearchRequestDto postSearchRequestDto = new PostSearchRequestDto(startDt, endDt, ownerType, city, district, sort);
    Pageable pageable1 = PageRequest.of(page, 8);

    return ResponseEntity.ok().body(postService.getPostsTermsSearch(pageable1, postSearchRequestDto));
  }
}

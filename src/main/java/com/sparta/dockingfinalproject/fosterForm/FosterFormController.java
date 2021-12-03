package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.fosterForm.dto.AcceptanceRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FosterFormController {

  private final FosterFormService fosterFormService;

  @PostMapping("posts/{postId}/adoptions")
  public ResponseEntity<Map<String, Object>> addFosterForm(@PathVariable Long postId,
      @Valid @RequestBody FosterFormRequestDto formRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(fosterFormService.addFosterForm(postId, formRequestDto, userDetails));
  }

  @GetMapping("foster_forms/{fosterFormId}")
  public ResponseEntity<Map<String, Object>> getFosterForm(@PathVariable Long fosterFormId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(fosterFormService.getFosterForm(fosterFormId, userDetails));
  }

  @GetMapping("/user/requests")
  public ResponseEntity<Map<String, Object>> getMyFosterForms(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(fosterFormService.getMyFosterForms(userDetails));
  }

  @GetMapping("/user/posts")
  public ResponseEntity<Map<String, Object>> getMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(fosterFormService.getMyPosts(userDetails));
  }

  @PatchMapping("/foster_forms/{fosterFormId}/acceptance")
  public ResponseEntity<Map<String, Object>> acceptForms(@PathVariable Long fosterFormId,
      @Valid @RequestBody AcceptanceRequestDto acceptanceRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(fosterFormService.acceptForms(fosterFormId, acceptanceRequestDto, userDetails));
  }
}
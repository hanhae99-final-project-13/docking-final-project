package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.fosterForm.dto.AcceptanceRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

  // FosterForm 등록
  @PostMapping("posts/{postId}/adoptions")
  public Map<String, Object> addFosterForm(@PathVariable Long postId,
      @Valid @RequestBody FosterFormRequestDto formRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return fosterFormService.addFosterForm(postId, formRequestDto, userDetails);
  }

  // 입양신청서 상세 조회
  @GetMapping("foster_forms/{fosterFormId}")
  public Map<String, Object> getFosterForm(@PathVariable Long fosterFormId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return fosterFormService.getFosterForm(fosterFormId, userDetails);
  }

  // 내가 보낸 입양신청서 목록 조회
  @GetMapping("/user/requests")
  public Map<String, Object> getMyFosterForms(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return fosterFormService.getMyFosterForms(userDetails);
  }

  // 내가 올린 post 목록 조회
  @GetMapping("/user/posts")
  public Map<String, Object> getMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return fosterFormService.getMyPosts(userDetails);
  }

  // 입양신청서 승낙, 반려
  @PatchMapping("/foster_forms/{fosterFormId}/acceptance")
  public Map<String, Object> acceptForms(@PathVariable Long fosterFormId,
      @Valid @RequestBody AcceptanceRequestDto acceptanceRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return fosterFormService.acceptForms(fosterFormId, acceptanceRequestDto, userDetails);
  }
}
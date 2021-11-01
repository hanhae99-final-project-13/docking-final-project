package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FosterFormController {

  private final FosterFormService fosterFormService;

  // FosterForm 등록
  @PostMapping("/{postId}/adoptions")
  public Map<String, Object> addFosterForm(@PathVariable Long postId, @RequestBody
      FosterFormRequestDto formRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return fosterFormService.addFosterForm(postId, formRequestDto, userDetails);
  }

}

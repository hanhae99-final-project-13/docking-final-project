package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EducationController {

  private final EducationService eduFinishService;

  public EducationController(EducationService eduFinishServce) {
    this.eduFinishService = eduFinishServce;
  }

  @GetMapping("/quiz")
  public Map<String, Object> checkEduFinish (@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String edu) {
    System.out.println(edu);

    return eduFinishService.checkEduFinish(userDetails, edu);
  }

}

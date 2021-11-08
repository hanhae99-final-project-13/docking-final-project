package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EducationController {

  private final EducationService educationService;

  public EducationController(EducationService educationService){
	this.educationService = educationService;
  }

  @GetMapping("/quiz")
  public Map<String, Object> saveEducation(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String edu) {
	return educationService.saveEdu(userDetails,edu);
  }

}

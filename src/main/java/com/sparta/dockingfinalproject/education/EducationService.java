package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

  private final UserRepository userRepository;
  private final EducationRepository educationRepository;

  public EducationService(UserRepository userRepository, EducationRepository educationRepository) {
	this.userRepository = userRepository;
	this.educationRepository = educationRepository;
  }

  public Map<String, Object> saveEdu(UserDetailsImpl userDetails, String edu) {

	User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
	Education education = educationRepository.findByUser(user).orElse(null);
	Map<String, Object> data = new HashMap<>();
	data.put("msg", "클래스" + edu + "이 완료되었습니다.");

	if (edu.equals("1")) {
	  education.setBasic(true);
	} else if (edu.equals("2")) {
	  education.setAdvanced(true);
	} else if (edu.equals("3")) {
	  education.setCore(true);
	} else {
	  throw new DockingException(ErrorCode.CLASSNUMBER_NOT_FOUND);
	}
	educationRepository.save(education);

	return SuccessResult.success(data);

  }

}

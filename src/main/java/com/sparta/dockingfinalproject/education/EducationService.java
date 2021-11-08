package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EducationService {
  private final UserRepository userRepository;
  private final EducationRepository educationRepository;

  public EducationService(UserRepository userRepository, EducationRepository educationRepository){
	this.userRepository = userRepository;
	this.educationRepository = educationRepository;
  }

  public void saveEdu(UserDetailsImpl userDetails, String edu) {

	User user = userRepository.findById(userDetails.getUser().getUserId()).orElseThrow(
		()-> new DockingException(ErrorCode.USER_NOT_FOUND)
	);
	Education education = educationRepository.findByUser(user).orElse(null);
//	List<Education> education = educationRepository.findByUser(user);
	if(edu.equals("1")){
	  education.setBasic(true);
	}else if(edu.equals("2")){
	  education.setAdvanced(true);
	}else if(edu.equals("3")){
	  education.setCore(true);
	}
	educationRepository.save(education);
  }
}

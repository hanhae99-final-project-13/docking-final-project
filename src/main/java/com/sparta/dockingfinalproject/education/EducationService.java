package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EducationService {

  private final EducationRepository eduFinishRepository;

  public EducationService(EducationRepository eduFinishRepository) {
	this.eduFinishRepository = eduFinishRepository;
  }

  public Map<String, Object> checkEduFinish(UserDetailsImpl userDetails, String edu) {


//	List<Education> doneEdu = educationRepository.findAllByEdu(education);
	Education eduFinish = new Education(userDetails.getUser(), edu);
	eduFinishRepository.save(eduFinish);


	Map<String, Object> data = new HashMap<>();
	data.put("msg", "교육이수가 완료되었습니다");

	return SuccessResult.success(data);
  }
}

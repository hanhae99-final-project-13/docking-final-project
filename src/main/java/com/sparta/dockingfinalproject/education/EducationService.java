package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
@Service
public class EducationService {


  private final EducationRepository educationRepository;
  public EducationService(EducationRepository educationRepository) {
	this.educationRepository = educationRepository;
  }

  public Map<String, Object> saveEdu(UserDetailsImpl userDetails, String edu) {

	User user = userDetails.getUser();
	Education education = educationRepository.findByUser(user).orElse(null);
	Map<String, Object> data = new HashMap<>();
	data.put("msg", "클래스" + edu + "이 완료되었습니다.");
	List<Map<String,Object>> eduList = new ArrayList<>();
	data.put("eduList", eduList);
	Map<String,Object> eduMap = new HashMap<>();

	if (edu.equals("1")) {
	  education.setBasic(true);
	} else if (edu.equals("2")) {
	  education.setAdvanced(true);
	} else if (edu.equals("3")) {
	  education.setCore(true);
	} else {
	  throw new DockingException(ErrorCode.CLASSNUMBER_NOT_FOUND);
	}

	eduMap.put("필수지식",education.getBasic());
	eduMap.put("심화지식", education.getAdvanced());
	eduMap.put("심화지식2", education.getCore());
	eduList.add(eduMap);

	educationRepository.save(education);

	return SuccessResult.success(data);

  }


}

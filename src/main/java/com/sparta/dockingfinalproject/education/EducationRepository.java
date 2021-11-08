package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education, Long> {
List<Education> findByUserAndEdu(User user, String edu);

}

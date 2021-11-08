package com.sparta.dockingfinalproject.education;

import com.sparta.dockingfinalproject.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education, Long> {

  Optional<Education> findByUser(User user);

}

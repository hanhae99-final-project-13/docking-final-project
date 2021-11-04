package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findAllByOrderByModifiedAtDesc(Pageable pageable);

  Optional<Post> findAllByPet(Pet pet);

  List<Post> findAllByUser(User user);
}

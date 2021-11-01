package com.sparta.dockingfinalproject.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findAllByOrderByModifiedAtDesc(Pageable pageable);
}

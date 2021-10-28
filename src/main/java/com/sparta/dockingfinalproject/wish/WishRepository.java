package com.sparta.dockingfinalproject.wish;

import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
  Optional<Wish> findAllByUserAndPost(User user, Post post);
}

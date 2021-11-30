package com.sparta.dockingfinalproject.wish.repository;

import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.wish.Wish;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long>, WishRepositoryCustom {

  Optional<Wish> findAllByUserAndPost(User user, Post post);
}

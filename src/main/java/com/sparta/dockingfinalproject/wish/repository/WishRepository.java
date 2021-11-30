package com.sparta.dockingfinalproject.wish.repository;

import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.wish.Wish;
import com.sparta.dockingfinalproject.wish.dto.WishResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

  Optional<Wish> findAllByUserAndPost(User user, Post post);

  List<WishResponseDto> findAllByUser(User user);
}

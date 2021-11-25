package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentResponseDto;
import com.sparta.dockingfinalproject.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<CommentResponseDto> findAllByPostOrderByCreatedAtDesc(Post post);
}

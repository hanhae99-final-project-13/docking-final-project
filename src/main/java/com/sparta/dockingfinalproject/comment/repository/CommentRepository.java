package com.sparta.dockingfinalproject.comment.repository;

import com.sparta.dockingfinalproject.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}

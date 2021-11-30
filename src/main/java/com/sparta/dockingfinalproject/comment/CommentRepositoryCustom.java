package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentResultDto;
import com.sparta.dockingfinalproject.post.model.Post;
import java.util.List;

public interface CommentRepositoryCustom {

  List<CommentResultDto> findAllPostInComment(Post post);
}

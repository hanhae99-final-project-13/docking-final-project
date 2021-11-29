package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.post.dto.PostDetailResponseDto;
import com.sparta.dockingfinalproject.post.dto.PostSearchRequestDto;
import com.sparta.dockingfinalproject.post.dto.PostSearchResponseDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

  Page<PostSearchResponseDto> searchPagePost(Pageable pageable, PostSearchRequestDto postSearchRequestDto);
  PostDetailResponseDto findPostDetail(Long postId, UserDetailsImpl userDetails);
}

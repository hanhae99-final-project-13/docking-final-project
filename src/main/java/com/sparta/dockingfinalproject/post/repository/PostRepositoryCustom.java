package com.sparta.dockingfinalproject.post.repository;

import com.sparta.dockingfinalproject.post.dto.request.PostSearchRequestDto;
import com.sparta.dockingfinalproject.post.dto.response.PostDetailResponseDto;
import com.sparta.dockingfinalproject.post.dto.response.PostPreviewDto;
import com.sparta.dockingfinalproject.post.dto.response.PostSearchResponseDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

  Page<PostSearchResponseDto> searchPagePost(Pageable pageable, PostSearchRequestDto postSearchRequestDto);
  PostDetailResponseDto findPostDetail(Long postId, UserDetailsImpl userDetails);
  List<PostPreviewDto> findHomePosts(Pageable pageable);
}

package com.sparta.dockingfinalproject.post;

import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public void getPosts(Long postId) {
    postRepository.findById(postId);
  }
}

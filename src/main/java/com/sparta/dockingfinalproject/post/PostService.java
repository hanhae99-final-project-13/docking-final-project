package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.dto.PostDetailResponseDto;
import com.sparta.dockingfinalproject.wish.Wish;
import com.sparta.dockingfinalproject.wish.WishRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final WishRepository wishRepository;

  public PostService(PostRepository postRepository, WishRepository wishRepository) {
    this.postRepository = postRepository;
    this.wishRepository = wishRepository;
  }

  public Map<String, Object> getPosts(Long postId, UserDetails userDetails) {
    Post findPost = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    Wish findWish = wishRepository.findAllByUserAndPost(userDetails.getUser(), findPost);

    boolean heart = false;
    if (findWish != null) {
      heart = true;
    }

    PostDetailResponseDto postResponseDto = PostDetailResponseDto.getPostDetailResponseDto(findPost.getPet(), heart);

    Map<String, Object> data = new HashMap<>();
    data.put("post", postResponseDto);
    data.put("commentList", findPost.getCommentList());
    return SuccessResult.success(data);
  }

  public Map<String, Object> addPost(PetRequestDto petRequestDto, UserDetails userDetails) {
    Pet pet = new Pet(petRequestDto);
    Post post = new Post(pet, userDetails.getUser());

    return SuccessResult.success("");
  }
}

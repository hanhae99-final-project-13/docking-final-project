package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.alarm.AlarmRepositoroy;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetRepository;
import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.dto.PostDetailResponseDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.wish.Wish;
import com.sparta.dockingfinalproject.wish.WishRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final WishRepository wishRepository;
  private final PetRepository petRepository;
  private final AlarmRepositoroy alarmRepositoroy;


  public PostService(PostRepository postRepository, WishRepository wishRepository, PetRepository petRepository, AlarmRepositoroy alarmRepositoroy) {
    this.postRepository = postRepository;
    this.wishRepository = wishRepository;
    this.petRepository = petRepository;
    this.alarmRepositoroy = alarmRepositoroy;
  }

  public Map<String, Object> home(UserDetailsImpl userDetails) {
    Pageable pageable = PageRequest.of(0, 6);
    Page<Post> postPage = postRepository.findAllByOrderByModifiedAtDesc(pageable);
    List<Post> posts = postPage.getContent();

    List<PostDetailResponseDto> postList = new ArrayList<>();
    for (Post post : posts) {
      PostDetailResponseDto postDetailResponseDto = PostDetailResponseDto.getPostDetailResponseDto(post);
      postList.add(postDetailResponseDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("postList", postList);
    data.put("alarmCount", alarmRepositoroy.findAllByUserAndStatusTrueOrderByCreatedAtDesc(userDetails.getUser()));

    return SuccessResult.success(data);
  }

  @Transactional
  public Map<String, Object> getPosts(Long postId, UserDetailsImpl userDetails) {
    Post findPost = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    Optional<Wish> findWish = wishRepository.findAllByUserAndPost(userDetails.getUser(), findPost);

    boolean heart = false;
    if (findWish.isPresent()) {
      heart = true;
    }
    findPost.addViewCount();
    PostDetailResponseDto postResponseDto = PostDetailResponseDto
        .getPostDetailResponseDto(findPost, heart);

    Map<String, Object> data = new HashMap<>();
    data.put("post", postResponseDto);
//    data.put("commentList", findPost.getCommentList());
    return SuccessResult.success(data);
  }

  @Transactional
  public Map<String, Object> addPost(PetRequestDto petRequestDto, UserDetailsImpl userDetails) {
    Pet pet = new Pet(petRequestDto);
    Pet savePet = petRepository.save(pet);

    Post post = new Post(savePet, userDetails.getUser());
    postRepository.save(post);

    return SuccessResult.success(new ArrayList<>());
  }

  @Transactional
  public Map<String, Object> updatePost(Long postId, PetRequestDto petRequestDto) {
    Post findPost = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    Pet pet = findPost.getPet();
    pet.update(petRequestDto);
    findPost.addPet(pet);

    Map<String, String> data = new HashMap<>();
    data.put("msg", "수정 완료");
    return SuccessResult.success(data);
  }

  @Transactional
  public Map<String, Object> deletePost(Long postId) {
    Post findPost = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    Pet findPet = findPost.getPet();

    postRepository.deleteById(findPost.getPostId());
    petRepository.deleteById(findPet.getPetId());

    Map<String, Object> data = new HashMap<>();
    data.put("msg", "삭제 완료");
    return SuccessResult.success(data);
  }
}

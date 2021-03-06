package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.comment.dto.CommentResultDto;
import com.sparta.dockingfinalproject.comment.repository.CommentRepository;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.dto.request.PostSearchRequestDto;
import com.sparta.dockingfinalproject.post.dto.request.StatusDto;
import com.sparta.dockingfinalproject.post.dto.response.PostDetailResponseDto;
import com.sparta.dockingfinalproject.post.dto.response.PostPreviewDto;
import com.sparta.dockingfinalproject.post.dto.response.PostSearchResponseDto;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.post.pet.PetRepository;
import com.sparta.dockingfinalproject.post.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.pet.model.IsAdopted;
import com.sparta.dockingfinalproject.post.pet.model.Pet;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PostService {

  private final PostRepository postRepository;
  private final PetRepository petRepository;
  private final CommentRepository commentRepository;
  private final AlarmRepository alarmRepository;

  public PostService(PostRepository postRepository, PetRepository petRepository, CommentRepository commentRepository,
      AlarmRepository alarmRepository) {
    this.postRepository = postRepository;
    this.petRepository = petRepository;
    this.commentRepository = commentRepository;
    this.alarmRepository = alarmRepository;
  }

  public Map<String, Object> home(UserDetailsImpl userDetails) {
    Map<String, Object> data = new HashMap<>();
    data.put("postList", getHomePosts());
    data.put("alarmCount", getAlarmCount(userDetails));

    return SuccessResult.success(data);
  }

  private List<PostPreviewDto> getHomePosts() {
    Pageable pageable = PageRequest.of(0, 6);
    return postRepository.findHomePosts(pageable);
  }

  private int getAlarmCount(UserDetailsImpl userDetails) {
    if (userDetails != null) {
      return alarmRepository.findAllByUserAndCheckedTrueOrderByCreatedAtDesc(userDetails.getUser()).size();
    }
    return 0;
  }

  @Transactional
  public Map<String, Object> getPost(Long postId, UserDetailsImpl userDetails) {
    PostDetailResponseDto postDetail = postRepository.findPostDetail(postId, userDetails);
    Post findPost = bringPost(postId);
    findPost.addViewCount();

    Map<String, Object> data = new HashMap<>();
    data.put("post", postDetail);
    data.put("commentList", getCommentList(findPost));

    return SuccessResult.success(data);
  }

  private List<CommentResultDto> getCommentList(Post findPost) {
    return commentRepository.findAllPostInComment(findPost);
  }

  @Transactional
  public Map<String, Object> addPost(PetRequestDto petRequestDto, UserDetailsImpl userDetails) {
    Pet pet = new Pet(petRequestDto);
    Pet savePet = petRepository.save(pet);

    if (userDetails != null) {
      Post post = new Post(savePet, userDetails.getUser());
      postRepository.save(post);
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }

    return SuccessResult.success(new ArrayList<>());
  }

  @Transactional
  public Map<String, Object> updatePost(Long postId, PetRequestDto petRequestDto, UserDetailsImpl userDetails) {
    Post findPost = bringPost(postId);
    Map<String, String> data = new HashMap<>();
    Long userId = userDetails.getUser().getUserId();
    Long writerId = findPost.getUser().getUserId();

    if (userId.equals(writerId)) {
      Pet pet = findPost.getPet();
      pet.update(petRequestDto);
      findPost.addPet(pet);

      data.put("msg", "?????? ??????");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  @Transactional
  public Map<String, Object> deletePost(Long postId, UserDetailsImpl userDetails) {
    Post findPost = bringPost(postId);
    Pet findPet = findPost.getPet();
    Long userId = userDetails.getUser().getUserId();
    Long writerId = findPost.getUser().getUserId();

    Map<String, Object> data = new HashMap<>();

    if (userId.equals(writerId)) {
      postRepository.deleteById(findPost.getPostId());
      petRepository.deleteById(findPet.getPetId());

      data.put("msg", "?????? ??????");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  // ?????? ?????? ????????????
  @Transactional
  public Map<String, Object> updateStatus(Long postId, StatusDto statusDto,
      UserDetailsImpl userDetails) {
    IsAdopted newStatus = IsAdopted.of(statusDto.getNewStatus());

    Long userId = userDetails.getUser().getUserId();

    Post findPost = bringPost(postId);
    Long writerId = findPost.getUser().getUserId();

    Map<String, String> data = new HashMap<>();

    // ?????? ?????? ??????????????????
    if (userId.equals(writerId)) {
      Pet findPet = findPost.getPet();
      IsAdopted status = findPet.getIsAdopted();
      String isAdopted = "";

      if (newStatus.equals(status)) {
        throw new DockingException(ErrorCode.NO_DIFFERENCE);
      } else if (newStatus.equals(IsAdopted.of("adopted"))) {
        isAdopted = "adopted";
      } else if (newStatus.equals(IsAdopted.of("abandoned"))) {
        isAdopted = "abandoned";
      } else {
        throw new IllegalArgumentException("????????? ??????????????? ????????????.");
      }

      findPet.updateStatus(isAdopted);

      data.put("msg", "??????????????? <" + isAdopted + ">(???)??? ?????????????????????.");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  // postId??? ?????? Post ????????????
  private Post bringPost(Long postId) {
    return postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
  }

  public Map<String, Object> getPostsTermsSearch(Pageable pageable1, PostSearchRequestDto postSearchRequestDto) {
    Page<PostSearchResponseDto> pagePost = postRepository.searchPagePost(pageable1, postSearchRequestDto);
    Map<String, Object> data = new HashMap<>();
    data.put("postList", pagePost.getContent());
    data.put("last", pagePost.isLast());
    data.put("totalPages", pagePost.getTotalPages());
    return SuccessResult.success(data);
  }
}
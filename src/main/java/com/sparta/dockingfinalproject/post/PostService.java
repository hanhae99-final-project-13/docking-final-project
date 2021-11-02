package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.alarm.AlarmRepositoroy;
import com.sparta.dockingfinalproject.comment.CommentRepository;
import com.sparta.dockingfinalproject.comment.dto.CommentResponseDto;
import com.sparta.dockingfinalproject.comment.dto.CommentResultDto;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetRepository;
import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.dto.PostDetailResponseDto;
import com.sparta.dockingfinalproject.post.dto.StatusDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.wish.Wish;
import com.sparta.dockingfinalproject.wish.WishRepository;
import java.time.LocalDateTime;
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
  private final CommentRepository commentRepository;
  private final AlarmRepositoroy alarmRepositoroy;

  Map<String, String> data = new HashMap<>();

  public PostService(PostRepository postRepository, WishRepository wishRepository,
      PetRepository petRepository, CommentRepository commentRepository,
      AlarmRepositoroy alarmRepositoroy) {
    this.postRepository = postRepository;
    this.wishRepository = wishRepository;
    this.petRepository = petRepository;
    this.commentRepository = commentRepository;
    this.alarmRepositoroy = alarmRepositoroy;
  }

  public Map<String, Object> home(UserDetailsImpl userDetails) {
    Pageable pageable = PageRequest.of(0, 6);
    Page<Post> postPage = postRepository.findAllByOrderByModifiedAtDesc(pageable);
    List<Post> posts = postPage.getContent();

    List<PostDetailResponseDto> postList = new ArrayList<>();
    for (Post post : posts) {
      PostDetailResponseDto postDetailResponseDto = PostDetailResponseDto.getPostDetailResponseDto(
          post);
      postList.add(postDetailResponseDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("postList", postList);
    data.put("alarmCount",
        alarmRepositoroy.findAllByUserAndStatusTrueOrderByCreatedAtDesc(userDetails.getUser()));

    return SuccessResult.success(data);
  }

  @Transactional
  public Map<String, Object> getPost(Long postId, UserDetailsImpl userDetails) {
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

    //Comment return data 가공하기
    ArrayList<CommentResultDto> commentDtoList = new ArrayList<>();
    CommentResultDto commentResultDto = new CommentResultDto();

    List<CommentResponseDto> commentResponseDto = commentRepository.findAllByPost(findPost);
    for (CommentResponseDto crd : commentResponseDto) {
      Long commentId = crd.getCommentId();
      String comment = crd.getComment();
      LocalDateTime createdAt = crd.getCreatedAt();
      LocalDateTime modifiedAt = crd.getModifiedAt();
      String nickname = crd.getUser().getNickname();

      commentResultDto = new CommentResultDto(commentId, comment, nickname, createdAt, modifiedAt);
      commentDtoList.add(commentResultDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("post", postResponseDto);
    data.put("commentList", commentDtoList);

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

  // 보호 상태 변경하기
  @Transactional
  public Map<String, Object> updateStatus(Long postId, StatusDto statusDto,
      UserDetailsImpl userDetails) {
    String newStatus = statusDto.getNewStatus();

    Long userId = userDetails.getUser().getUserId();

    Post findPost = bringPost(postId);
    Long writerId = findPost.getUser().getUserId();

    // 보호 상태 업데이트하기
    if (userId.equals(writerId)) {
      Pet findPet = findPost.getPet();
      String status = findPet.getIsAdopted();
      String isAdopted = "";

      if (newStatus.equals(status)) {
        throw new DockingException(ErrorCode.NO_DIFFERENCE);
      } else if (newStatus.equals("보호종료")) {
        isAdopted = "보호종료";
      } else if (newStatus.equals("입양진행중")) {
        isAdopted = "입양진행중";
      }

      findPet.updateStatus(isAdopted);

      data.put("msg", "보호상태가 <" + isAdopted + ">(으)로 변경되었습니다.");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  // postId로 해당 Post 가져오기
  private Post bringPost(Long postId) {
    return postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
  }
}
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
import com.sparta.dockingfinalproject.post.dto.PostSearchResponseDto;
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
    Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
    List<Post> posts = postPage.getContent();

    Map<String, Object> data = new HashMap<>();
    data.put("postList", getPostList(posts));
    data.put("alarmCount", getAlarmCount(userDetails));

    return SuccessResult.success(data);
  }

  private List<PostDetailResponseDto> getPostList(List<Post> posts) {
    List<PostDetailResponseDto> postList = new ArrayList<>();
    for (Post post : posts) {
      PostDetailResponseDto postDetailResponseDto = PostDetailResponseDto.getPostDetailResponseDto(post);
      postList.add(postDetailResponseDto);
    }
    return postList;
  }

  private int getAlarmCount(UserDetailsImpl userDetails) {
    if (userDetails != null) {
      return alarmRepositoroy.findAllByUserAndStatusTrueOrderByCreatedAtDesc(userDetails.getUser()).size();
    }
    return 0;
  }

  @Transactional
  public Map<String, Object> getPost(Long postId, UserDetailsImpl userDetails) {
    Post findPost = bringPost(postId);
    boolean heart = getHeart(userDetails, findPost);

    findPost.addViewCount();

    PostDetailResponseDto postResponseDto = PostDetailResponseDto.getPostDetailResponseDto(findPost, heart);

    Map<String, Object> data = new HashMap<>();
    data.put("post", postResponseDto);
    data.put("commentList", getCommentList(findPost));

    return SuccessResult.success(data);
  }

  private boolean getHeart(UserDetailsImpl userDetails, Post findPost) {
    if (userDetails != null) {
      Optional<Wish> findWish = wishRepository.findAllByUserAndPost(userDetails.getUser(), findPost);
      if (findWish.isPresent()) {
        return true;
      }
    }
    return false;
  }

  //Comment return data 가공하기
  private ArrayList<CommentResultDto> getCommentList(Post findPost) {
    ArrayList<CommentResultDto> commentDtoList = new ArrayList<>();

    List<CommentResponseDto> commentResponseDto = commentRepository.findAllByPost(findPost);
    for (CommentResponseDto crd : commentResponseDto) {
      CommentResultDto commentResultDto = getCommentResult(crd);
      commentDtoList.add(commentResultDto);
    }
    return commentDtoList;
  }

  private CommentResultDto getCommentResult(CommentResponseDto crd) {
    Long commentId = crd.getCommentId();
    String comment = crd.getComment();
    LocalDateTime createdAt = crd.getCreatedAt();
    LocalDateTime modifiedAt = crd.getModifiedAt();
    String nickname = crd.getUser().getNickname();

    return new CommentResultDto(commentId, comment, nickname, createdAt, modifiedAt);
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

      data.put("msg", "수정 완료");
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

      data.put("msg", "삭제 완료");
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
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

    Map<String, String> data = new HashMap<>();

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

  public Map<String, Object> getPostsTermsSearch(Pageable pageable, String startDt, String endDt, String ownerType, String city, String district, String sort) {
    Page<Pet> pets = null;
    if (sort.equalsIgnoreCase("new")) {
      if (startDt != null) {
        String[] starts = startDt.split("-");
        String[] ends = endDt.split("-");
        LocalDateTime start = LocalDateTime.of(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]), 0, 0);
        LocalDateTime end = LocalDateTime.of(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]), 23, 59);
        if (ownerType == null) {
          if (city == null) {
            pets = petRepository.findAllByCreatedAtBetweenOrderByCreatedAtDesc(start, end, pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByCreatedAtBetweenAndAddressLikeOrderByCreatedAtDesc(start, end, address, pageable);
          }
        }

        if (ownerType != null) {
          if (city == null) {
            pets = petRepository.findAllByCreatedAtBetweenAndOwnerTypeContainingOrderByCreatedAtDesc(start, end, ownerType, pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByCreatedAtBetweenAndOwnerTypeContainingAndAddressLikeOrderByCreatedAtDesc(start, end, ownerType, address, pageable);
          }
        }
      }

      if (startDt == null) {
        if (ownerType == null) {
          if (city == null) {
            pets = petRepository.findAllByOrderByCreatedAtDesc(pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByAddressLikeOrderByCreatedAtDesc(address, pageable);
          }
        }

        if (ownerType != null) {
          if (city == null) {
            pets = petRepository.findAllByOwnerTypeContainingOrderByCreatedAtDesc(ownerType, pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByOwnerTypeAndAddressLikeOrderByCreatedAtDesc(ownerType, address, pageable);
          }
        }
      }
    }

    if (sort.equalsIgnoreCase("old")) {
      if (startDt != null) {
        String[] starts = startDt.split("-");
        String[] ends = endDt.split("-");
        LocalDateTime start = LocalDateTime.of(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]), 0, 0);
        LocalDateTime end = LocalDateTime.of(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]), 23, 59);

        if (ownerType == null) {
          if (city == null) {
            pets = petRepository.findAllByCreatedAtBetweenOrderByCreatedAtAsc(start, end, pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByCreatedAtBetweenAndAddressLikeOrderByCreatedAtAsc(start, end, address, pageable);
          }
        }

        if (ownerType != null) {
          if (city == null) {
            pets = petRepository.findAllByCreatedAtBetweenAndOwnerTypeContainingOrderByCreatedAtAsc(start, end, ownerType, pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByCreatedAtBetweenAndOwnerTypeContainingAndAddressLikeOrderByCreatedAtAsc(start, end, ownerType, address, pageable);
          }
        }
      }

      if (startDt == null) {
        if (ownerType == null) {
          if (city == null) {
            pets = petRepository.findAllByOrderByCreatedAtAsc(pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByAddressLikeOrderByCreatedAtAsc(address, pageable);
          }
        }

        if (ownerType != null) {
          if (city == null) {
            pets = petRepository.findAllByOwnerTypeContainingOrderByCreatedAtAsc(ownerType, pageable);
          }

          if (city != null) {
            String address = city + " " + district;
            pets = petRepository.findAllByOwnerTypeContainingAndAddressLikeOrderByCreatedAtAsc(ownerType, address, pageable);
          }
        }
      }
    }

    List<PostSearchResponseDto> postList = new ArrayList<>();
    for (Pet pet : pets.getContent()) {
      Post post = postRepository.findAllByPet(pet).orElseThrow(
          () -> new DockingException(ErrorCode.PET_NOT_FOUND)
      );

      List<String> imgs = new ArrayList<>();
      String[] str = pet.getImg().split(" ## ");
      for (String x : str) {
        imgs.add(x);
      }
      PostSearchResponseDto postSearchResponseDto = PostSearchResponseDto.builder()
          .userId(post.getUser().getUserId())
          .nickname(post.getUser().getNickname())
          .postId(post.getPostId())
          .createdAt(pet.getCreatedAt())
          .modifiedAt(pet.getModifiedAt())
          .breed(pet.getBreed())
          .sex(pet.getSex())
          .age(pet.getAge())
          .ownerType(pet.getOwnerType())
          .address(pet.getAddress())
          .img(imgs)
          .isAdopted(pet.getIsAdopted())
          .build();

      postList.add(postSearchResponseDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("postList", postList);
    data.put("last", pets.isLast());
    data.put("totalPages", pets.getTotalPages());
    return SuccessResult.success(data);
  }
}
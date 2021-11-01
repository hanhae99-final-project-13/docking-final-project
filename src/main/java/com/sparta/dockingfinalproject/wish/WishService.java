package com.sparta.dockingfinalproject.wish;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.wish.dto.WishResponseDto;
import com.sparta.dockingfinalproject.wish.dto.WishResultDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WishService {

  private final WishRepository wishRepository;
  private final PostRepository postRepository;

  public WishService(WishRepository wishRepository, PostRepository postRepository) {
    this.wishRepository = wishRepository;
    this.postRepository = postRepository;
  }

  public Map<String, Object> addWish(Long postId, UserDetailsImpl userDetails) {
    Map<String, Object> data = new HashMap<>();

    Post findPost = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    Optional<Wish> findWish = wishRepository.findAllByUserAndPost(userDetails.getUser(), findPost);
    if (findWish.isPresent()) {
      Long wishId = findWish.get().getWishId();
      wishRepository.deleteById(wishId);

      data.put("msg", "관심목록에서 삭제되었습니다.");
      return SuccessResult.success(data);
    }

    Wish wish = new Wish();
    wish.addPost(findPost);
    wish.addUser(userDetails.getUser());

    wishRepository.save(wish);

    data.put("msg", "관심목록에 추가되었습니다.");

    return SuccessResult.success(data);
  }


  // 나의 WishList 조회
  public Map<String, Object> getWishes(User user) {

    ArrayList<WishResultDto> wishResultDtos = new ArrayList<>();
    WishResultDto wishResultDto = new WishResultDto();

    List<WishResponseDto> wishResponseDtos = wishRepository.findAllByUser(user);

    for (WishResponseDto wrd : wishResponseDtos) {
      Post post = wrd.getPost();
      Pet pet = post.getPet();

      Long wishId = wrd.getWishId();
      Long postId = post.getPostId();
      LocalDateTime createdAt = post.getCreatedAt();
      LocalDateTime modifiedAt = post.getModifiedAt();
      String breed = pet.getBreed();
      Sex sex = pet.getSex();
      int age = pet.getAge();
      String ownerType = pet.getOwnerType();
      String address = pet.getAddress();
      String img = pet.getImg();
      String isAdopted = pet.getIsAdopted();

      wishResultDto = new WishResultDto(wishId, postId, createdAt, modifiedAt, breed, sex, age,
          ownerType, address, img, isAdopted);
      wishResultDtos.add(wishResultDto);

    }

    Map<String, Object> data = new HashMap<>();
    data.put("wishList", wishResultDtos);

    return SuccessResult.success(data);

  }
}

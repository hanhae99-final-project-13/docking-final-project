package com.sparta.dockingfinalproject.wish;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.wish.dto.WishResultDto;
import com.sparta.dockingfinalproject.wish.repository.WishRepository;
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

  public Map<String, Object> addWishAndDeleteWish(Long postId, UserDetailsImpl userDetails) {
    Map<String, Object> data = new HashMap<>();

    Post findPost = findPost(postId);

    Optional<Wish> findWish = wishRepository.findAllByUserAndPost(userDetails.getUser(), findPost);
    if (findWish.isPresent()) {
      deleteWish(findWish.get().getWishId());
      data.put("msg", "관심목록에서 삭제되었습니다.");
      return SuccessResult.success(data);
    }

    saveWish(findPost, userDetails.getUser());
    data.put("msg", "관심목록에 추가되었습니다.");
    return SuccessResult.success(data);
  }

  private Post findPost(Long postId) {
    return postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
  }

  private void deleteWish(Long wishId) {
    wishRepository.deleteById(wishId);
  }

  private void saveWish(Post post, User user) {
    Wish wish = new Wish();
    wish.addPost(post);
    wish.addUser(user);
    wishRepository.save(wish);
  }

  // 나의 WishList 조회
  public Map<String, Object> getWishes(User user) {
    List<WishResultDto> myWishs = wishRepository.findAllMyWishs(user);

    Map<String, Object> data = new HashMap<>();
    data.put("wishList", myWishs);

    return SuccessResult.success(data);

  }
}

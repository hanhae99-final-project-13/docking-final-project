package com.sparta.dockingfinalproject.wish;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.util.HashMap;
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
}

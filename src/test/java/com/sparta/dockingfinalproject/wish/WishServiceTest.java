package com.sparta.dockingfinalproject.wish;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.Sex;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import com.sparta.dockingfinalproject.wish.dto.WishResponseDto;
import com.sparta.dockingfinalproject.wish.dto.WishResultDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

  @InjectMocks
  private WishService wishService;

  @Mock
  private PostRepository postRepository;

  @Mock
  private WishRepository wishRepository;

  @Mock
  private UserRepository userRepository;

  private Pet pet;
  private User user;
  private UserDetailsImpl userDetails;
  private Post post;
  private Post post1;
  private Post post2;

  private Wish wish;
  private Wish wish1;
  private Wish wish2;

  @BeforeEach
  void init() {
	pet = new Pet(10L, "요크셔", Sex.M, 1, 3.5, "남양주시", "경기도 남양주시 도농동",
		"010-1234-1236", "친근", "https://www.naver.com", IsAdopted.ADOPTED,

		"보호소", "10", "귀여움", "https://www.naver.com",
		new Post());

	user = new User(1L, "user1", "aa1234", "홍길동", "sss@naver.com", "", "", 0L, "", true, "");
	userDetails = new UserDetailsImpl(user);

	post = new Post(100L, 0L, pet, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	post1 = new Post(101L, 0L, pet, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	post2 = new Post(102L, 0L, pet, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

	wish = new Wish(20L, user, post);
	wish1 = new Wish(21L, user, post1);
	wish2 = new Wish(22L, user, post2);
  }

  @Test
  @DisplayName("관심목록 등록하기")
  void addWish() {
	when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));

	wishService.addWishAndDeleteWish(post.getPostId(), userDetails);

	verify(wishRepository, times(1)).findAllByUserAndPost(user, post);
  }

  @Test
  @DisplayName("관심목록 삭제하기")
  void deleteWish() {
	when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
	when(wishRepository.findAllByUserAndPost(user, post)).thenReturn(Optional.of(wish));

	Map<String, Object> deleteResult = wishService.addWishAndDeleteWish(post.getPostId(),
		userDetails);
	Map<String, Object> data = (Map<String, Object>) deleteResult.get("data");

	assertThat(data.get("msg")).isEqualTo("관심목록에서 삭제되었습니다.");
  }

  @Test
  @DisplayName("로그인 유저 관심목록 조회")
  void getUserWishs() {
	List<WishResponseDto> wishList = new ArrayList<>();

	wishList.add(new WishResponseDto() {
	  @Override
	  public Long getWishId() {
		return wish.getWishId();
	  }

	  @Override
	  public Post getPost() {
		return wish.getPost();
	  }
	});

	wishList.add(new WishResponseDto() {
	  @Override
	  public Long getWishId() {
		return wish1.getWishId();
	  }

	  @Override
	  public Post getPost() {
		return wish1.getPost();
	  }
	});

	wishList.add(new WishResponseDto() {
	  @Override
	  public Long getWishId() {
		return wish2.getWishId();
	  }

	  @Override
	  public Post getPost() {
		return wish2.getPost();
	  }
	});

	when(wishRepository.findAllByUser(user)).thenReturn(wishList);

	Map<String, Object> data = (Map<String, Object>) wishService.getWishes(user).get("data");
	List<WishResultDto> wishResultDtos = (List<WishResultDto>) data.get("wishList");

	assertThat(wishResultDtos.get(0).getWishId()).isEqualTo(20L);
	assertThat(wishResultDtos.get(1).getWishId()).isEqualTo(21L);
	assertThat(wishResultDtos.get(2).getWishId()).isEqualTo(22L);
  }
}
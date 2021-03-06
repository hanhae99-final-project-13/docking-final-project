package com.sparta.dockingfinalproject.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.comment.repository.CommentRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.post.pet.PetRepository;
import com.sparta.dockingfinalproject.post.pet.model.IsAdopted;
import com.sparta.dockingfinalproject.post.pet.model.Pet;
import com.sparta.dockingfinalproject.post.pet.model.Sex;
import com.sparta.dockingfinalproject.post.pet.dto.PetRequestDto;
import com.sparta.dockingfinalproject.post.dto.response.PostPreviewDto;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.model.User;
import com.sparta.dockingfinalproject.user.repository.UserRepository;
import com.sparta.dockingfinalproject.wish.repository.WishRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @InjectMocks
  private PostService postService;

  @Mock
  private PostRepository postRepository;

  @Mock
  private WishRepository wishRepository;

  @Mock
  private PetRepository petRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private AlarmRepository alarmRepository;

  @Mock
  private UserRepository userRepository;

  private PetRequestDto petRequestDto;
  private Pet pet;
  private Pet pet1;
  private Pet pet2;
  private Pet pet3;
  private Pet pet4;
  private Pet pet5;
  private Post post;
  private Post post1;
  private Post post2;
  private Post post3;
  private Post post4;
  private Post post5;
  private UserDetailsImpl userDetails;
  private User user;

  @BeforeEach
  void init() {
    List<String> temp = new ArrayList<>();
    temp.add("https://www.naver.com");
    petRequestDto = PetRequestDto.builder()
        .breed("?????????")
        .sex("m")
        .age(1)
        .weight(3.5)
        .lostLocation("????????????")
        .ownerType("?????????")
        .address("????????? ???????????? ?????????")
        .phone("010-1234-1236")
        .url("https://www.naver.com")
        .img(temp)
        .extra("?????????")
        .isAdopted("ADOPTED")
        .build();

    pet = new Pet(10L, "?????????", Sex.M, 1, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "??????", "https://www.naver.com", IsAdopted.ADOPTED,
        "?????????", "10", "?????????", "https://www.naver.com",
        new Post());

    pet1 = new Pet(11L, "?????????", Sex.M, 1, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "??????", "https://www.naver.com", IsAdopted.ADOPTED,
        "?????????", "10", "?????????", "https://www.naver.com",
        new Post());

    pet2 = new Pet(12L, "?????????", Sex.M, 1, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "??????", "https://www.naver.com", IsAdopted.ADOPTED,
        "?????????", "10", "?????????", "https://www.naver.com",
        new Post());

    pet3 = new Pet(13L, "?????????", Sex.M, 1, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "??????", "https://www.naver.com", IsAdopted.ADOPTED,
        "?????????", "10", "?????????", "https://www.naver.com",
        new Post());

    pet4 = new Pet(14L, "?????????", Sex.M, 1, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "??????", "https://www.naver.com", IsAdopted.ADOPTED,
        "?????????", "10", "?????????", "https://www.naver.com",
        new Post());

    pet5 = new Pet(15L, "?????????", Sex.M, 1, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "??????", "https://www.naver.com", IsAdopted.ADOPTED,
        "?????????", "10", "?????????", "https://www.naver.com",
        new Post());

    user = new User(1L, "user1", "aa1234", "?????????", "sss@naver.com", "", "", 0L, "");

    userDetails = new UserDetailsImpl(user);

    post = new Post(100L, 0L, pet, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    post1 = new Post(101L, 0L, pet1, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    post2 = new Post(102L, 0L, pet2, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    post3 = new Post(103L, 0L, pet3, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    post4 = new Post(104L, 0L, pet4, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    post5 = new Post(105L, 0L, pet5, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

  }

  @Test
  @DisplayName("Pet ??????, Post ??????")
  void addPost() {
    Post addPost = new Post();
    Pet addPet = new Pet();

    when(petRepository.findById(10L)).thenReturn(Optional.of(addPet));
    when(postRepository.findById(100L)).thenReturn(Optional.of(addPost));

    postService.addPost(petRequestDto, userDetails);

    assertThat(petRepository.findById(10L).get().getPetId()).isEqualTo(addPet.getPetId());
    assertThat(postRepository.findById(100L).get().getPostId()).isEqualTo(addPost.getPostId());

  }

  @Test
  @DisplayName("home ?????? 6??? ????????? ??????")
  void getHomePosts() {
    Pageable pageable = PageRequest.of(0, 6);
    List<PostPreviewDto> postPreviewDtoList = new ArrayList<>();
    postPreviewDtoList.add(PostPreviewDto.of(post));
    postPreviewDtoList.add(PostPreviewDto.of(post1));
    postPreviewDtoList.add(PostPreviewDto.of(post2));
    postPreviewDtoList.add(PostPreviewDto.of(post3));
    postPreviewDtoList.add(PostPreviewDto.of(post4));
    postPreviewDtoList.add(PostPreviewDto.of(post5));

    when(postRepository.findHomePosts(pageable)).thenReturn(postPreviewDtoList);

    Map<String, Object> home = postService.home(userDetails);
    Map<String, Object> data = (Map<String, Object>) home.get("data");
    List<PostPreviewDto> postList = (List<PostPreviewDto>) data.get("postList");

    assertThat(postList.size()).isEqualTo(6);
    assertThat(postList.get(0).getPostId()).isEqualTo(100L);
    assertThat(postList.get(1).getPostId()).isEqualTo(101L);
    assertThat(postList.get(2).getPostId()).isEqualTo(102L);
    assertThat(postList.get(3).getPostId()).isEqualTo(103L);
    assertThat(postList.get(4).getPostId()).isEqualTo(104L);
    assertThat(postList.get(5).getPostId()).isEqualTo(105L);
  }

  @Test
  @DisplayName("????????? ?????? ??????")
  void getPost() {
    when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));

    assertThat(postRepository.findById(100L).get().getPostId()).isEqualTo(post.getPostId());
  }

  @Test
  @DisplayName("????????? ?????? ??????_????????? ?????? ??????")
  void getPostNotFound() {
    when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));

    assertThat(postRepository.findById(100L).get().getPostId()).isEqualTo(post.getPostId());
    assertThrows(DockingException.class,
        () -> postService.getPost(99L, userDetails), "?????? ???????????? ?????? ??? ????????????.");
  }

  @Test
  @DisplayName("????????? ??????")
  void updatePost() {
    when(postRepository.findById(100L)).thenReturn(Optional.of(post));

    List<String> temp = new ArrayList<>();
    temp.add("https://www.naver.com");
    petRequestDto = PetRequestDto.builder()
        .breed("??????????????????")
        .sex("m")
        .age(2)
        .weight(3.8)
        .lostLocation("?????????")
        .ownerType("???????????????")
        .address("????????? ?????????")
        .phone("010-1234-1236")
        .url("https://www.naver.com")
        .img(temp)
        .extra("?????????")
        .isAdopted("ADOPTED")

        .build();

    postService.updatePost(post.getPostId(), petRequestDto, userDetails);

    Pet updatePet = post.getPet();
    assertThat(updatePet.getBreed()).isEqualTo("??????????????????");
    assertThat(updatePet.getAge()).isEqualTo(2);
    assertThat(updatePet.getLostLocation()).isEqualTo("?????????");
    assertThat(updatePet.getOwnerType()).isEqualTo("???????????????");
    assertThat(updatePet.getAddress()).isEqualTo("????????? ?????????");
  }

  @Test
  @DisplayName("????????? ?????? ??????")
  void updatePostFail() {

  }

  @Test
  @DisplayName("????????? ??????")
  void deletePost() {
//    when(petRepository.findById(10L)).thenReturn(Optional.of(pet));
//    when(postRepository.findById(100L)).thenReturn(Optional.of(post));
//
//    postService.deletePost(post.getPostId(), userDetails);
//
//    assertThat(postRepository.getById(100L)).isNull();
  }
}

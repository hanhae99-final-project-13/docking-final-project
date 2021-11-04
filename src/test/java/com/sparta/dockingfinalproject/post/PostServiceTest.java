//  package com.sparta.dockingfinalproject.post;
//
//  import static org.assertj.core.api.Assertions.assertThat;
//  import static org.junit.jupiter.api.Assertions.assertThrows;
//  import static org.mockito.Mockito.when;
//
//  import com.sparta.dockingfinalproject.alarm.AlarmRepositoroy;
//  import com.sparta.dockingfinalproject.comment.CommentRepository;
//  import com.sparta.dockingfinalproject.exception.DockingException;
//  import com.sparta.dockingfinalproject.pet.Pet;
//  import com.sparta.dockingfinalproject.pet.PetRepository;
//  import com.sparta.dockingfinalproject.pet.Sex;
//  import com.sparta.dockingfinalproject.pet.dto.PetRequestDto;
//  import com.sparta.dockingfinalproject.post.dto.PostDetailResponseDto;
//  import com.sparta.dockingfinalproject.security.UserDetailsImpl;
//  import com.sparta.dockingfinalproject.user.User;
//  import com.sparta.dockingfinalproject.user.UserRepository;
//  import com.sparta.dockingfinalproject.wish.WishRepository;
//  import java.util.ArrayList;
//  import java.util.List;
//  import java.util.Map;
//  import java.util.Optional;
//  import org.junit.jupiter.api.BeforeEach;
//  import org.junit.jupiter.api.DisplayName;
//  import org.junit.jupiter.api.Test;
//  import org.junit.jupiter.api.extension.ExtendWith;
//  import org.mockito.InjectMocks;
//  import org.mockito.Mock;
//  import org.mockito.junit.jupiter.MockitoExtension;
//  import org.springframework.data.domain.Page;
//  import org.springframework.data.domain.PageImpl;
//  import org.springframework.data.domain.PageRequest;
//  import org.springframework.data.domain.Pageable;
//
//  @ExtendWith(MockitoExtension.class)
//  class PostServiceTest {
//
//    @InjectMocks
//    private PostService postService;
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private WishRepository wishRepository;
//
//    @Mock
//    private PetRepository petRepository;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private AlarmRepositoroy alarmRepositoroy;
//
//    @Mock
//    private UserRepository userRepository;
//
//    private PetRequestDto petRequestDto;
//    private Pet pet;
//    private Post post;
//    private Post post1;
//    private Post post2;
//    private Post post3;
//    private Post post4;
//    private Post post5;
//    private UserDetailsImpl userDetails;
//    private User user;
//
//    @BeforeEach
//    void init() {
//      petRequestDto = PetRequestDto.builder()
//          .breed("요크셔")
//          .sex("m")
//          .age(1)
//          .weight(3.5)
//          .lostLocation("남양주시")
//          .ownerType("보호소")
//          .address("경기도 남양주시 도농동")
//          .phone("010-1234-1236")
//          .url("https://www.naver.com")
//          .img("https://www.naver.com")
//          .extra("귀여움")
//          .isAdopted("true")
//          .build();
//
//      pet = new Pet(10L, "요크셔", Sex.M, 1, 3.5, "남양주시", "경기도 남양주시 도농동",
//          "010-1234-1236", "친근", "https://www.naver.com", "true",
//          "보호소", "10", "귀여움", "https://www.naver.com",
//          new Post());
//
//      user = new User(1L, "user1", "aa1234", "홍길동", "sss@naver.com", "", "",  0L, "", true, new ArrayList<>());
//
//      userDetails = new UserDetailsImpl(user);
//
//      post = new Post(100L, 0L, pet, user, new ArrayList<>(), new ArrayList<>());
//      post1 = new Post(101L, 0L, pet, user, new ArrayList<>(), new ArrayList<>());
//      post2 = new Post(102L, 0L, pet, user, new ArrayList<>(), new ArrayList<>());
//      post3 = new Post(103L, 0L, pet, user, new ArrayList<>(), new ArrayList<>());
//      post4 = new Post(104L, 0L, pet, user, new ArrayList<>(), new ArrayList<>());
//      post5 = new Post(105L, 0L, pet, user, new ArrayList<>(), new ArrayList<>());
//    }
//
//    @Test
//    @DisplayName("Pet 저장, Post 저장")
//    void addPost() {
//      Post addPost = new Post();
//      Pet addPet = new Pet();
//
//      when(petRepository.findById(10L)).thenReturn(Optional.of(addPet));
//      when(postRepository.findById(100L)).thenReturn(Optional.of(addPost));
//
//      postService.addPost(petRequestDto, userDetails);
//
//      assertThat(petRepository.findById(10L).get().getPetId()).isEqualTo(addPet.getPetId());
//      assertThat(postRepository.findById(100L).get().getPostId()).isEqualTo(addPost.getPostId());
//    }
//
//    @Test
//    @DisplayName("home 화면 6개 게시글 조회")
//    void getHomePosts() {
//      List<Post> posts = new ArrayList<>();
//      posts.add(post);
//      posts.add(post1);
//      posts.add(post2);
//      posts.add(post3);
//      posts.add(post4);
//      posts.add(post5);
//      Page<Post> pages = new PageImpl<>(posts);
//
//      Pageable pageable = PageRequest.of(0, 6);
//
//      when(postRepository.findAllByOrderByModifiedAtDesc(pageable)).thenReturn(pages);
//
//      Map<String, Object> home = postService.home(userDetails);
//      Map<String, Object> data = (Map<String, Object>) home.get("data");
//      List<PostDetailResponseDto> postList = (List<PostDetailResponseDto>) data.get("postList");
//
//      assertThat(postList.size()).isEqualTo(6);
//      assertThat(postList.get(0).getPostId()).isEqualTo(100L);
//      assertThat(postList.get(1).getPostId()).isEqualTo(101L);
//      assertThat(postList.get(2).getPostId()).isEqualTo(102L);
//      assertThat(postList.get(3).getPostId()).isEqualTo(103L);
//      assertThat(postList.get(4).getPostId()).isEqualTo(104L);
//      assertThat(postList.get(5).getPostId()).isEqualTo(105L);
//    }
//
//    @Test
//    @DisplayName("게시글 단건 조회")
//    void getPost() {
//      when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
//
//      assertThat(postRepository.findById(100L).get().getPostId()).isEqualTo(post.getPostId());
//    }
//
//    @Test
//    @DisplayName("게시글 단건 조회_게시글 없는 경우")
//    void getPostNotFound() {
//      when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
//
//      assertThat(postRepository.findById(100L).get().getPostId()).isEqualTo(post.getPostId());
//      assertThrows(DockingException.class,
//          () -> postService.getPost(99L, userDetails), "해당 게시글을 찾을 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("게시글 수정")
//    void updatePost() {
//      when(postRepository.findById(100L)).thenReturn(Optional.of(post));
//
//      petRequestDto = PetRequestDto.builder()
//          .breed("요크셔테리어")
//          .sex("m")
//          .age(2)
//          .weight(3.8)
//          .lostLocation("구리시")
//          .ownerType("입양진행중")
//          .address("경기도 구리시")
//          .phone("010-1234-1236")
//          .url("https://www.naver.com")
//          .img("https://www.naver.com")
//          .extra("귀여움")
//          .isAdopted("true")
//          .build();
//
//      postService.updatePost(post.getPostId(), petRequestDto, userDetails);
//
//      Pet updatePet = post.getPet();
//      assertThat(updatePet.getBreed()).isEqualTo("요크셔테리어");
//      assertThat(updatePet.getAge()).isEqualTo(2);
//      assertThat(updatePet.getLostLocation()).isEqualTo("구리시");
//      assertThat(updatePet.getOwnerType()).isEqualTo("입양진행중");
//      assertThat(updatePet.getAddress()).isEqualTo("경기도 구리시");
//    }
//
//    @Test
//    @DisplayName("게시글 수정 실패")
//    void updatePostFail() {
//
//    }
//
//    @Test
//    @DisplayName("게시글 삭제")
//    void deletePost() {
//  //    when(petRepository.findById(10L)).thenReturn(Optional.of(pet));
//  //    when(postRepository.findById(100L)).thenReturn(Optional.of(post));
//  //
//  //    postService.deletePost(post.getPostId(), userDetails);
//  //
//  //    assertThat(postRepository.getById(100L)).isNull();
//    }
//  }
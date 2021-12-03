package com.sparta.dockingfinalproject.fosterForm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.education.model.Education;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.dto.AcceptanceRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormResultDto;
import com.sparta.dockingfinalproject.fosterForm.dto.MyPostsResponseDto;
import com.sparta.dockingfinalproject.fosterForm.dto.MyRequestsDto;
import com.sparta.dockingfinalproject.fosterForm.model.Acceptance;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.post.pet.model.IsAdopted;
import com.sparta.dockingfinalproject.post.pet.model.Pet;
import com.sparta.dockingfinalproject.post.pet.model.Sex;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@ExtendWith(MockitoExtension.class)
class FosterFormServiceTest {

  @InjectMocks
  private FosterFormService fosterFormService;

  @Mock
  private FosterFormRepository fosterFormRepository;
  @Mock
  private PostRepository postRepository;
  @Mock
  private AlarmRepository alarmRepository;
  @Mock
  private SimpMessageSendingOperations simpMessageSendingOperations;
  @Mock
  private EducationRepository educationRepository;

  private User user01;
  private User user02;
  private UserDetailsImpl userDetailsNull;

  private Pet pet01;
  private Post post01;

  private UserDetailsImpl userDetails01;
  private UserDetailsImpl userDetails02;

  private FosterFormRequestDto fosterFormRequestDto;
  private FosterForm fosterForm;
  private AcceptanceRequestDto acceptanceRequestDto;
  private Education education;


  @BeforeEach
  void init() {
    // 사용자 Null
    userDetailsNull = null;

    // user01
    user01 = new User(101L, "user01", "aa1234", "테스트유저01", "testuser01@sparta.com", "", "", 111L,
        "01022223333");
    userDetails01 = new UserDetailsImpl(user01);

    // user01의 post01
    pet01 = new Pet(10L, "욕구싯타리야", Sex.of("m"), 2020, 3.5, "남양주시", "경기도 남양주시 도농동",
        "010-1234-1236", "직접등록", "https://www.naver.com", IsAdopted.ABANDONED,
        "개인", "", "귀여움", "https://www.naver.com",
        new Post());
    post01 = new Post(200L, 0L, pet01, user01, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    acceptanceRequestDto = AcceptanceRequestDto.builder()
        .acceptance("accepted")
        .build();

    // user02
    user02 = new User(102L, "user02", "aa1234", "테스트유저02", "testuser02@sparta.com", "", "", 112L,
        "01044443333");
    userDetails02 = new UserDetailsImpl(user02);
    education = new Education(610L, user02, true, true, false);

    // user02의 post01에 등록된 fosterForm
    fosterFormRequestDto = new FosterFormRequestDto("스폰지밥", 14L, "M", "01033330000", "집게리아 알바",
        "해저시 파인애플동", "달팽이1", "현재 키우는 중", "달팽이 친구 만들어주려고", "비염", "없음", "퇴근후 종일", "새로운 친구를 만들어준다",
        "애견 학교에 보낸다", "별가한테 맡긴다", " 50만원", "600만원",
        "https://static1.srcdn.com/wordpress/wp-content/uploads/2020/03/SpongeBob-SquarePants-bedroom.jpg?q=50&fit=crop&w=740&h=370&dpr=1.5",
        "https://mblogthumb-phinf.pstatic.net/MjAxNzAzMjNfMTEg/MDAxNDkwMjY4MDQ4MDUw.U3oWgUU609gMxMjMFmaa6xZHEZCpwQ1DMAqIBsgUAsQg.QPHZmN4JUKGZMaPGbs_L4Oep7JpwFXqqLwQ6hF6r3W0g.JPEG.ckt0409/SpongBob.SquarePants.S03E05a.As.Seen.On.TV.DVD.XViD-iND.avi_20170323_201236.268.jpg?type=w2",
        "꼭 제가 데려가고싶어요");
    fosterForm = FosterForm.builder()
        .FosterFormId(401L)
        .name("스폰지밥")
        .fosterAge(14L)
        .gender(Sex.of("m"))
        .phone("01033330000")
        .job("집게리아 알바")
        .fosterAddress("바다광역시 파인애플동")
        .currentPet("달팽이1")
        .experience("현재 키우는 중")
        .reason("달팽이 친구 만들어주려고")
        .allergy("비염")
        .family("없음")
        .timeTogether("퇴근후 종일")
        .anxiety("새로운 친구를 만들어준다")
        .bark("애견 학교에 보낸다")
        .leaveHome("별가한테 맡긴다")
        .medicalBudget("50만원")
        .monthlyBudget("600만원")
        .roomUrl(
            "https://static1.srcdn.com/wordpress/wp-content/uploads/2020/03/SpongeBob-SquarePants-bedroom.jpg?q=50&fit=crop&w=740&h=370&dpr=1.5")
        .signUrl(
            "https://mblogthumb-phinf.pstatic.net/MjAxNzAzMjNfMTEg/MDAxNDkwMjY4MDQ4MDUw.U3oWgUU609gMxMjMFmaa6xZHEZCpwQ1DMAqIBsgUAsQg.QPHZmN4JUKGZMaPGbs_L4Oep7JpwFXqqLwQ6hF6r3W0g.JPEG.ckt0409/SpongBob.SquarePants.S03E05a.As.Seen.On.TV.DVD.XViD-iND.avi_20170323_201236.268.jpg?type=w2")
        .etc("꼭 제가 데려가고싶어요")
        .acceptance(Acceptance.waiting)
        .user(user02)
        .post(post01)
        .build();
  }


  @Nested
  @DisplayName("FosterForm_저장")
  class addFosterForm {

    @Test
    @DisplayName("성공_케이스")
    void addSuccess() {
      // given
      when(postRepository.findById(200L)).thenReturn(Optional.of(post01));
      given(educationRepository.findByUser(any())).willReturn(Optional.of(education));
      // when
      Map<String, Object> successResult = fosterFormService.addFosterForm(200L,
          fosterFormRequestDto,
          userDetails02);
      Map<String, Object> data = (Map<String, Object>) successResult.get("data");
      // then
      verify(fosterFormRepository).save(any(FosterForm.class));
      assertThat(data.get("msg")).isEqualTo("입양 신청이 완료 되었습니다");
    }


    @Nested
    @DisplayName("실패_케이스")
    class failCases {

      @Test
      @DisplayName("사용자가_Null_일때")
      void addFail01() {
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.addFosterForm(200L, fosterFormRequestDto, userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("PostId가_Null_일때")
      void addFail02() {
        // given
        Long postId = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.addFosterForm(postId, fosterFormRequestDto, userDetails02);
        });
        //then
        assertEquals(ErrorCode.POST_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("Tag가_직접등록이_아닌_Post에_신청했을때")
      void addFail04() {
        // given
        pet01 = new Pet(10L, "욕구싯타리야", Sex.of("m"), 2020, 3.5, "남양주시", "경기도 남양주시 도농동",
            "010-1234-1236", "가져온 정보", "https://www.naver.com", IsAdopted.ABANDONED,
            "개인", "", "귀여움", "https://www.naver.com",
            new Post());
        post01 = new Post(200L, 0L, pet01, user01, new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>());
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.addFosterForm(post01.getPostId(), fosterFormRequestDto, userDetails02);
        });
        //then
        assertEquals(ErrorCode.POST_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("본인_게시글에_신청했을때")
      void addFail05() {
        // given
        when(postRepository.findById(200L)).thenReturn(Optional.of(post01));
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.addFosterForm(200L, fosterFormRequestDto, userDetails01);
        });
        //then
        assertEquals(ErrorCode.NOT_AVAILABLE_FOR_MINE, dockingException.getErrorCode());
      }
    }
  }


  @Nested
  @DisplayName("FosterForm_조회")
  class getFosterForm {

    @Test
    @DisplayName("성공_케이스")
    void getSuccess() {
      // given
      when(fosterFormRepository.findById(401L)).thenReturn(Optional.of(fosterForm));
      when(postRepository.findById(post01.getPostId())).thenReturn(Optional.of(post01));
      given(educationRepository.findByUser(any())).willReturn(Optional.of(education));
      // when
      Map<String, Object> successResult = fosterFormService.getFosterForm(
          fosterForm.getFosterFormId(), userDetails01);
      Map<String, Object> data = (Map<String, Object>) successResult.get("data");
      FosterFormResultDto fosterFormResultDto = (FosterFormResultDto) data.get("fosterForm");
      // then
      verify(fosterFormRepository).findById(fosterForm.getFosterFormId());

      assertThat(fosterFormResultDto.getFosterFormId()).isEqualTo(401L);
      assertThat(fosterFormResultDto.getName()).isEqualTo("스폰지밥");
      assertThat(fosterFormResultDto.getJob()).isEqualTo("집게리아 알바");
    }


    @Nested
    @DisplayName("실패_케이스")
    class failCases {

      @Test
      @DisplayName("사용자가_Null_일때")
      void getFail01() {
        // given
        userDetailsNull = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.getFosterForm(fosterForm.getFosterFormId(), userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("FosterFormId가_Null_일때")
      void getFail02() {
        // given
        Long fosterFormId = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.getFosterForm(fosterFormId, userDetails01);
        });
        // then
        assertEquals(ErrorCode.FOSTERFORM_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("조회권한이_없을때")
      void getFail03() {
        // given
        User user03 = new User(103L, "user03", "aa1234", "테스트유저03", "testuser03@sparta.com", "", "",
            113L,
            "01043124333");
        UserDetailsImpl userDetails03 = new UserDetailsImpl(user03);

        when(fosterFormRepository.findById(401L)).thenReturn(Optional.of(fosterForm));
        when(postRepository.findById(post01.getPostId())).thenReturn(Optional.of(post01));
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.getFosterForm(fosterForm.getFosterFormId(), userDetails03);
        });
        // then
        assertEquals(ErrorCode.NO_AUTHORIZATION, dockingException.getErrorCode());
      }

    }
  }


  @Nested
  @DisplayName("나의_FosterForms_조회")
  class getMyFosterForms {

    @Test
    @DisplayName("성공_케이스")
    void getMyFormsSuccess() {
      // given
      List<FosterForm> fosterForms = new ArrayList<>();
      fosterForms.add(fosterForm);

      when(fosterFormRepository.findAllByUser(user02)).thenReturn(fosterForms);
      // when
      Map<String, Object> successResult = fosterFormService.getMyFosterForms(userDetails02);
      Map<String, Object> data = (Map<String, Object>) successResult.get("data");
      List<MyRequestsDto> myRequestsDtos = (List<MyRequestsDto>) data.get("myRequestList");
      // then
      verify(fosterFormRepository).findAllByUser(any());

      assertThat(myRequestsDtos.get(0).getFosterFormId()).isEqualTo(401L);
      assertThat(myRequestsDtos.get(0).getAcceptance()).isEqualTo(Acceptance.waiting);
    }


    @Nested
    @DisplayName("실패_케이스")
    class failCases {

      @Test
      @DisplayName("사용자가_Null_일때")
      void getMyFormsFail01() {
        // given
        userDetailsNull = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.getMyFosterForms(userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

    }
  }


  @Nested
  @DisplayName("나의_Post에_들어온_입양신청목록_조회")
  class getMyPosts {

    @Test
    @DisplayName("성공_케이스")
    void getMyPostSuccess() {
      // given
      List<FosterForm> formList = new ArrayList<>();
      formList.add(fosterForm);

      post01 = new Post(200L, 0L, pet01, user01, new ArrayList<>(), new ArrayList<>(),
          formList);

      List<Post> myPosts = new ArrayList<>();
      myPosts.add(post01);

      when(postRepository.findAllByUser(user01)).thenReturn(myPosts);
      given(educationRepository.findByUser(any())).willReturn(Optional.of(education));
      // when
      Map<String, Object> successResult = fosterFormService.getMyPosts(userDetails01);
      Map<String, Object> data = (Map<String, Object>) successResult.get("data");
      List<MyPostsResponseDto> MyPostsResponseDto = (List<MyPostsResponseDto>) data.get(
          "fosterFormsInMyPosts");
      // then
      verify(postRepository).findAllByUser(any());

      assertThat(MyPostsResponseDto.get(0).getPostPreview().getPostId()).isEqualTo(200L);
      assertThat(MyPostsResponseDto.get(0).getFormPreviews().get(0).getFosterFormId()).isEqualTo(
          401L);
    }

    @Nested
    @DisplayName("실패_케이스")
    class failCases {

      @Test
      @DisplayName("사용자가_Null_일때")
      void getMyFormsFail01() {
        // given
        userDetailsNull = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.getMyPosts(userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

    }
  }


  @Nested
  @DisplayName("입양신청서_승낙_반려")
  class acceptForms {

    @Test
    @DisplayName("성공_케이스")
    void acceptSuccess() {
      // given
      when(fosterFormRepository.findById(fosterForm.getFosterFormId())).thenReturn(
          Optional.of(fosterForm));
      when(postRepository.findById(post01.getPostId())).thenReturn(Optional.of(post01));
      // when
      Map<String, Object> successResult = fosterFormService.acceptForms(
          fosterForm.getFosterFormId(), acceptanceRequestDto, userDetails01);
      Map<String, Object> data = (Map<String, Object>) successResult.get("data");
      // then
      assertThat(successResult.get("status")).isEqualTo("success");
      assertThat(data.get("msg")).isEqualTo(
          "승낙여부가 <" + acceptanceRequestDto.getAcceptance() + ">(으)로 변경되었습니다.");
    }

    @Nested
    @DisplayName("실패_케이스")
    class failCases {

      @Test
      @DisplayName("사용자가_Null_일때")
      void acceptFail01() {
        // given
        userDetailsNull = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.acceptForms(
              fosterForm.getFosterFormId(), acceptanceRequestDto, userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }
    }

    @Test
    @DisplayName("FosterFormId가_Null_일때")
    void getFail02() {
      // given
      Long fosterFormId = null;
      // when
      DockingException dockingException = assertThrows(DockingException.class, () -> {
        fosterFormService.acceptForms(
            fosterFormId, acceptanceRequestDto, userDetails01);
      });
      // then
      assertEquals(ErrorCode.FOSTERFORM_NOT_FOUND, dockingException.getErrorCode());
    }

  }
}
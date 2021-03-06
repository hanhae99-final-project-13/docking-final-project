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
    // ????????? Null
    userDetailsNull = null;

    // user01
    user01 = new User(101L, "user01", "aa1234", "???????????????01", "testuser01@sparta.com", "", "", 111L,
        "01022223333");
    userDetails01 = new UserDetailsImpl(user01);

    // user01??? post01
    pet01 = new Pet(10L, "??????????????????", Sex.of("m"), 2020, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "????????????", "https://www.naver.com", IsAdopted.ABANDONED,
        "??????", "", "?????????", "https://www.naver.com",
        new Post());
    post01 = new Post(200L, 0L, pet01, user01, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    acceptanceRequestDto = AcceptanceRequestDto.builder()
        .acceptance("accepted")
        .build();

    // user02
    user02 = new User(102L, "user02", "aa1234", "???????????????02", "testuser02@sparta.com", "", "", 112L,
        "01044443333");
    userDetails02 = new UserDetailsImpl(user02);
    education = new Education(610L, user02, true, true, false);

    // user02??? post01??? ????????? fosterForm
    fosterFormRequestDto = new FosterFormRequestDto("????????????", 14L, "M", "01033330000", "???????????? ??????",
        "????????? ???????????????", "?????????1", "?????? ????????? ???", "????????? ?????? ??????????????????", "??????", "??????", "????????? ??????", "????????? ????????? ???????????????",
        "?????? ????????? ?????????", "???????????? ?????????", " 50??????", "600??????",
        "https://static1.srcdn.com/wordpress/wp-content/uploads/2020/03/SpongeBob-SquarePants-bedroom.jpg?q=50&fit=crop&w=740&h=370&dpr=1.5",
        "https://mblogthumb-phinf.pstatic.net/MjAxNzAzMjNfMTEg/MDAxNDkwMjY4MDQ4MDUw.U3oWgUU609gMxMjMFmaa6xZHEZCpwQ1DMAqIBsgUAsQg.QPHZmN4JUKGZMaPGbs_L4Oep7JpwFXqqLwQ6hF6r3W0g.JPEG.ckt0409/SpongBob.SquarePants.S03E05a.As.Seen.On.TV.DVD.XViD-iND.avi_20170323_201236.268.jpg?type=w2",
        "??? ?????? ?????????????????????");
    fosterForm = FosterForm.builder()
        .FosterFormId(401L)
        .name("????????????")
        .fosterAge(14L)
        .gender(Sex.of("m"))
        .phone("01033330000")
        .job("???????????? ??????")
        .fosterAddress("??????????????? ???????????????")
        .currentPet("?????????1")
        .experience("?????? ????????? ???")
        .reason("????????? ?????? ??????????????????")
        .allergy("??????")
        .family("??????")
        .timeTogether("????????? ??????")
        .anxiety("????????? ????????? ???????????????")
        .bark("?????? ????????? ?????????")
        .leaveHome("???????????? ?????????")
        .medicalBudget("50??????")
        .monthlyBudget("600??????")
        .roomUrl(
            "https://static1.srcdn.com/wordpress/wp-content/uploads/2020/03/SpongeBob-SquarePants-bedroom.jpg?q=50&fit=crop&w=740&h=370&dpr=1.5")
        .signUrl(
            "https://mblogthumb-phinf.pstatic.net/MjAxNzAzMjNfMTEg/MDAxNDkwMjY4MDQ4MDUw.U3oWgUU609gMxMjMFmaa6xZHEZCpwQ1DMAqIBsgUAsQg.QPHZmN4JUKGZMaPGbs_L4Oep7JpwFXqqLwQ6hF6r3W0g.JPEG.ckt0409/SpongBob.SquarePants.S03E05a.As.Seen.On.TV.DVD.XViD-iND.avi_20170323_201236.268.jpg?type=w2")
        .etc("??? ?????? ?????????????????????")
        .acceptance(Acceptance.waiting)
        .user(user02)
        .post(post01)
        .build();
  }


  @Nested
  @DisplayName("FosterForm_??????")
  class addFosterForm {

    @Test
    @DisplayName("??????_?????????")
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
      assertThat(data.get("msg")).isEqualTo("?????? ????????? ?????? ???????????????");
    }


    @Nested
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
      void addFail01() {
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          fosterFormService.addFosterForm(200L, fosterFormRequestDto, userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("PostId???_Null_??????")
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
      @DisplayName("Tag???_???????????????_??????_Post???_???????????????")
      void addFail04() {
        // given
        pet01 = new Pet(10L, "??????????????????", Sex.of("m"), 2020, 3.5, "????????????", "????????? ???????????? ?????????",
            "010-1234-1236", "????????? ??????", "https://www.naver.com", IsAdopted.ABANDONED,
            "??????", "", "?????????", "https://www.naver.com",
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
      @DisplayName("??????_????????????_???????????????")
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
  @DisplayName("FosterForm_??????")
  class getFosterForm {

    @Test
    @DisplayName("??????_?????????")
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
      assertThat(fosterFormResultDto.getName()).isEqualTo("????????????");
      assertThat(fosterFormResultDto.getJob()).isEqualTo("???????????? ??????");
    }


    @Nested
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
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
      @DisplayName("FosterFormId???_Null_??????")
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
      @DisplayName("???????????????_?????????")
      void getFail03() {
        // given
        User user03 = new User(103L, "user03", "aa1234", "???????????????03", "testuser03@sparta.com", "", "",
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
  @DisplayName("??????_FosterForms_??????")
  class getMyFosterForms {

    @Test
    @DisplayName("??????_?????????")
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
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
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
  @DisplayName("??????_Post???_?????????_??????????????????_??????")
  class getMyPosts {

    @Test
    @DisplayName("??????_?????????")
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
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
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
  @DisplayName("???????????????_??????_??????")
  class acceptForms {

    @Test
    @DisplayName("??????_?????????")
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
          "??????????????? <" + acceptanceRequestDto.getAcceptance() + ">(???)??? ?????????????????????.");
    }

    @Nested
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
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
    @DisplayName("FosterFormId???_Null_??????")
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
package com.sparta.dockingfinalproject.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.comment.dto.CommentEditRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.comment.dto.CommentResultDto;
import com.sparta.dockingfinalproject.comment.model.Comment;
import com.sparta.dockingfinalproject.comment.repository.CommentRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.post.pet.model.IsAdopted;
import com.sparta.dockingfinalproject.post.pet.model.Pet;
import com.sparta.dockingfinalproject.post.pet.model.Sex;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.model.User;
import java.util.ArrayList;
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
class CommentServiceTest {

  @InjectMocks
  private CommentService commentService;

  @Mock
  private PostRepository postRepository;
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private AlarmRepository alarmRepository;
  @Mock
  private SimpMessageSendingOperations simpMessageSendingOperations;

  private UserDetailsImpl userDetails02;
  private UserDetailsImpl userDetailsNull;
  private Post post01;
  private Comment comment01;
  private Comment updatedComment;
  private CommentRequestDto commentRequestDto;
  private CommentEditRequestDto commentEditRequestDto;


  @BeforeEach
  void init() {
    // ????????? Null
    userDetailsNull = null;

    // user01
    User user01 = new User(101L, "user01", "aa1234", "???????????????01", "testuser01@sparta.com", "", "",
        111L,
        "01022223333");

    // user01??? post01
    Pet pet01 = new Pet(10L, "??????????????????", Sex.M, 2020, 3.5, "????????????", "????????? ???????????? ?????????",
        "010-1234-1236", "????????????", "https://www.naver.com", IsAdopted.ABANDONED,
        "??????", "", "?????????", "https://www.naver.com",
        new Post());
    post01 = new Post(200L, 0L, pet01, user01, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());

    // user02
    User user02 = new User(102L, "user02", "aa1234", "???????????????02", "testuser02@sparta.com", "", "",
        112L,
        "01044443333");
    userDetails02 = new UserDetailsImpl(user02);

    // Dtos
    commentRequestDto = CommentRequestDto.builder()
        .postId(200L)
        .comment("<???????????????> ???????????? ?????? ??????????????? ????????????")
        .build();
    commentEditRequestDto = new CommentEditRequestDto("<?????????????????????> ????????? ?????? ??????????????????");

    // post01??? ????????? user02??? comment01
    comment01 = new Comment(301L, commentRequestDto.getComment(), post01, user02);
    updatedComment = new Comment(comment01.getCommentId(), commentEditRequestDto.getComment(),
        post01, user02);
  }


  @Nested
  @DisplayName("Comment_??????")
  class addComment {

    @Test
    @DisplayName("??????_?????????")
    void addSuccess() {
      // given
      given(commentRepository.save(any())).willReturn(comment01);

      when(postRepository.findById(commentRequestDto.getPostId())).thenReturn(Optional.of(post01));
      // when
      Map<String, Object> successResult = commentService.addComment(commentRequestDto,
          userDetails02);
      Map<String, Object> data = (Map<String, Object>) successResult.get("data");
      CommentResultDto commentResultDto = (CommentResultDto) data.get("newComment");
      // then
      assertThat(commentResultDto.getCommentId()).isEqualTo(comment01.getCommentId());
      assertThat(commentResultDto.getComment()).isEqualTo(comment01.getComment());
      assertThat(commentResultDto.getNickname()).isEqualTo(comment01.getUser().getNickname());
    }


    @Nested
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
      void addFail01() {
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          commentService.addComment(commentRequestDto,
              userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("postId???_Null_??????")
      void addFail02() {
        // given
        commentRequestDto = CommentRequestDto.builder()
            .postId(null)
            .comment("<???????????????> ???????????? ?????? ??????????????? ????????????")
            .build();
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          commentService.addComment(commentRequestDto,
              userDetails02);
        });
        // then
        assertEquals(ErrorCode.POST_NOT_FOUND, dockingException.getErrorCode());
      }

    }
  }


  @Nested
  @DisplayName("Comment_??????")
  class updateComment {

    @Test
    @DisplayName("??????_?????????")
    void updateSuccess() {
      // given
      given(commentRepository.save(any())).willReturn(updatedComment);

      when(commentRepository.findById(301L)).thenReturn(Optional.of(comment01));
      // when
      Map<String, Object> SuccessResult = commentService.updateComment(301L,
          commentEditRequestDto,
          userDetails02);
      Map<String, Object> data = (Map<String, Object>) SuccessResult.get("data");
      CommentResultDto commentResultDto = (CommentResultDto) data.get("updatedComment");
      // then
      assertThat(commentResultDto.getCommentId()).isEqualTo(updatedComment.getCommentId());
      assertThat(commentResultDto.getComment()).isEqualTo(updatedComment.getComment());
      assertThat(commentResultDto.getNickname()).isEqualTo(
          updatedComment.getUser().getNickname());
    }


    @Nested
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
      void updateFail01() {
        //when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          commentService.updateComment(301L, commentEditRequestDto,
              userDetailsNull);
        });
        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("commentId???_Null_??????")
      void updateFail02() {
        // given
        Long commentId = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          commentService.updateComment(commentId, commentEditRequestDto,
              userDetails02);
        });
        // then
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, dockingException.getErrorCode());
      }

    }
  }


  @Nested
  @DisplayName("Comment_??????")
  class deleteComment {

//    @Test
//    @DisplayName("??????_?????????")
//    void deleteSuccess() {
//      // given
//      when(commentRepository.findById(301L)).thenReturn(Optional.of(comment01));
//      // when
//      Map<String, Object> successResult = commentService.deleteComment(comment01.getCommentId(),
//          userDetails02);
//      // then
//      verify(commentRepository, times(1)).findById(301L);
//      verify(commentRepository, times(1)).deleteById(301L);
//
//      assertThat(successResult.get("status")).isEqualTo("success");
//    }


    @Nested
    @DisplayName("??????_?????????")
    class failCases {

      @Test
      @DisplayName("????????????_Null_??????")
      void deleteFail01() {
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          commentService.deleteComment(301L, userDetailsNull);
        });
        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, dockingException.getErrorCode());
      }

      @Test
      @DisplayName("commentId???_Null_??????")
      void deleteFail02() {
        // given
        Long commentId = null;
        // when
        DockingException dockingException = assertThrows(DockingException.class, () -> {
          commentService.deleteComment(commentId, userDetails02);
        });
        // then
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, dockingException.getErrorCode());
      }

    }
  }
}

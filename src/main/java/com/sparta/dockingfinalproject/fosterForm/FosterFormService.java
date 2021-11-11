package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.dto.AcceptanceRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormPreviewDto;
import com.sparta.dockingfinalproject.fosterForm.dto.MyPostsResponseDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormResultDto;
import com.sparta.dockingfinalproject.fosterForm.dto.MyRequestsDto;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.post.dto.PostPreviewDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FosterFormService {

  private final FosterFormRepository fosterFormRepository;
  private final PostRepository postRepository;


  //입양신청서 등록
  @Transactional
  public Map<String, Object> addFosterForm(Long postId, FosterFormRequestDto fosterFormRequestDto,
      UserDetailsImpl userDetails) {

    User user = bringUser(userDetails);
    Post findPost = bringPost(postId);
    validateTag(findPost);
    checkDuplicateRequest(user, findPost);

    Acceptance acceptance = Acceptance.valueOf("waiting");
    FosterForm fosterForm = new FosterForm(findPost, fosterFormRequestDto, user, acceptance);
    fosterFormRepository.save(fosterForm);

    Map<String, Object> data = new HashMap<>();
    data.put("msg", "입양 신청이 완료 되었습니다");
    return SuccessResult.success(data);
  }


  //입양 신청서 상세조회
  @Transactional
  public Map<String, Object> getFosterForm(Long fosterFormId, UserDetailsImpl userDetails) {

    User user = bringUser(userDetails);
    FosterForm findFosterForm = bringFosterForm(fosterFormId);
    checkFosterFormAccess(user, findFosterForm);

    FosterFormResultDto fosterFormResultDto = FosterFormResultDto.of(findFosterForm);
    Map<String, Object> data = new HashMap<>();
    data.put("fosterForm", fosterFormResultDto);
    return SuccessResult.success(data);
  }


  //내가 보낸 입양신청서 목록조회
  @Transactional
  public Map<String, Object> getMyFosterForms(UserDetailsImpl userDetails) {

    User user = bringUser(userDetails);

    List<FosterForm> fosterForms = bringFosterForms(user);

    List<MyRequestsDto> myRequestsDtos = new ArrayList<>();
    for (FosterForm fosterForm : fosterForms) {
      Post post = fosterForm.getPost();
      PostPreviewDto postPreviewDto = PostPreviewDto.of(post);
      MyRequestsDto myRequestsDto = MyRequestsDto.of(fosterForm, postPreviewDto);

      myRequestsDtos.add(myRequestsDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("myRequestList", myRequestsDtos);
    return SuccessResult.success(data);
  }


  //내가 올린 포스트와 각 포스트에 들어온 입양신청 목록조회
  @Transactional
  public Map<String, Object> getMyPosts(UserDetailsImpl userDetails) {

    User user = bringUser(userDetails);
    List<Post> myPosts = postRepository.findAllByUser(user);
    List<MyPostsResponseDto> myPostsResponseDtos = new ArrayList<>();

    for (Post post : myPosts) {
      PostPreviewDto postPreviewDto = PostPreviewDto.of(post);
      List<FosterFormPreviewDto> fosterFormPreviewDtos = new ArrayList<>();
      List<FosterForm> fosterForms = post.getFormList();

      for (FosterForm fosterForm : fosterForms) {
        FosterFormPreviewDto fosterFormPreviewDto = FosterFormPreviewDto.of(fosterForm);
        fosterFormPreviewDtos.add(fosterFormPreviewDto);
      }

      MyPostsResponseDto myPostsResponseDto = MyPostsResponseDto.of(postPreviewDto,
          fosterFormPreviewDtos);
      myPostsResponseDtos.add(myPostsResponseDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("fosterFormsInMyPosts", myPostsResponseDtos);
    return SuccessResult.success(data);
  }


  //입양신청서 승낙, 반려
  @Transactional
  public Map<String, Object> acceptForms(Long fosterFormId,
      AcceptanceRequestDto acceptanceRequestDto,
      UserDetailsImpl userDetails) {

    User user = bringUser(userDetails);
    FosterForm findFosterForm = bringFosterForm(fosterFormId);
    User postWriter = bringPostWriter(findFosterForm);
    postWriterAuthority(user, postWriter);

    Acceptance acceptance = findFosterForm.getAcceptance();
    Acceptance newAcceptance = Acceptance.of(acceptanceRequestDto.getAcceptance());
    Map<String, String> data = new HashMap<>();
    updateNewAcceptance(findFosterForm, data, acceptance, newAcceptance);
    return SuccessResult.success(data);
  }


  //중복 입양신청 체크
  private void checkDuplicateRequest(User user, Post findPost) {
    if (user.validateUser(findPost.getUser().getUserId())) {
      throw new DockingException(ErrorCode.REQUEST_DUPLICATE);
    } else {
      List<FosterForm> fosterForms = findPost.getFormList();
      for (FosterForm fosterform : fosterForms) {
        if (user.validateUser(bringFosterFormWriter(fosterform).getUserId())) {
          throw new DockingException(ErrorCode.REQUEST_DUPLICATE);
        }
      }
    }
  }

  //Post 태그가 직접등록인지 체크
  private void validateTag(Post findPost) {
    if (findPost.getPet().getTag() == null || !findPost.getPet().getTag().equals("직접등록")) {
      throw new DockingException(ErrorCode.NO_AVAILABILITY);
    }
  }

  //Acceptance 변경
  private void updateNewAcceptance(FosterForm findFosterForm, Map<String, String> data,
      Acceptance acceptance, Acceptance newAcceptance) {
    if (newAcceptance.equals(acceptance)) {
      throw new DockingException(ErrorCode.NO_DIFFERENCE);
    } else {
      findFosterForm.updateAcceptance(newAcceptance);
      data.put("msg", "승낙여부가 <" + newAcceptance + ">(으)로 변경되었습니다.");
    }
  }

  //FosterForm writer 가져오기
  private User bringFosterFormWriter(FosterForm findFosterForm) {
    return findFosterForm.getUser();
  }

  //Post writer 가져오기
  private User bringPostWriter(FosterForm findFosterForm) {
    Long postId = findFosterForm.getPost().getPostId();
    Post findPost = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
    return findPost.getUser();
  }

  //Post writer 권한체크
  private void postWriterAuthority(User user, User postWriter) {
    if (!user.validateUser(postWriter.getUserId())) {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
  }

  //입양신청서 조회 권한 체크
  private void checkFosterFormAccess(User user, FosterForm findFosterForm) {
    Long postWriterId = bringPostWriter(findFosterForm).getUserId();
    Long fosterFormWriterId = bringFosterFormWriter(findFosterForm).getUserId();
    if (!user.validateUser(postWriterId) && !user.validateUser(fosterFormWriterId)) {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
  }

  //로그인 체크 & User 가져오기
  private User bringUser(UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    } else {
      return userDetails.getUser();
    }
  }

  //해당 Post 가져오기
  private Post bringPost(Long postId) {
    return postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
  }

  //해당 FosterForm 가져오기
  private FosterForm bringFosterForm(Long fosterFormId) {
    return fosterFormRepository.findById(fosterFormId).orElseThrow(
        () -> new DockingException(ErrorCode.FOSTERFORM_NOT_FOUND)
    );
  }

  //내가 작성한 입양신청서 목록 가져오기
  private List<FosterForm> bringFosterForms(User user) {
    List<FosterForm> fosterForms = fosterFormRepository.findAllByUser(user);
    if (fosterForms.size() == 0) {
      throw new DockingException(ErrorCode.FOSTERFORM_NOT_FOUND);
    }
    return fosterForms;
  }

}


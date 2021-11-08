package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.dto.FormPreviewDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FormsInPostDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormResultDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterPreviewDto;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.post.dto.PostPreviewDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
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
  private final UserRepository userRepository;

  //입양신청서 등록
  @Transactional
  public Map<String, Object> addFosterForm(Long postId, FosterFormRequestDto fosterFormRequestDto,
      UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    }

    //User 가져오기
    User user = userDetails.getUser();

    //해당 Post 가져오기
    Post post = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
    if ( post.getPet().getTag() == null || !post.getPet().getTag().equals("직접등록") ) {
      throw new DockingException(ErrorCode.NO_AVAILABILITY);
    }
    List<FosterForm> fosterForms = post.getFormList();
    for (FosterForm fosterform : fosterForms) {
      if (fosterform.getUser() == user) {
        throw new DockingException(ErrorCode.REQUEST_DUPLICATE);
      }
    }

    //db에 FosterForm 저장
    FosterForm fosterForm = new FosterForm(post, fosterFormRequestDto, user);
    fosterFormRepository.save(fosterForm);

    //리턴 data 생성
    Map<String, Object> data = new HashMap<>();
    data.put("msg", "입양 신청이 완료 되었습니다");
    return SuccessResult.success(data);
  }

  //입양 신청서 상세조회
  @Transactional
  public Map<String, Object> getFosterForm(Long fosterFormId, UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    }
    Map<String, Object> data = new HashMap<>();

    Long userId = userDetails.getUser().getUserId();

    FosterForm findFosterForm = fosterFormRepository.findById(fosterFormId).orElseThrow(
        () -> new DockingException(ErrorCode.FOSTERFORM_NOT_FOUND)
    );

    Long postWriterId = findFosterForm.getPost().getUser().getUserId();
    Long writerId = findFosterForm.getUser().getUserId();
    if (userId.equals(writerId) || userId.equals(postWriterId)) {
      FosterFormResultDto fosterFormResultDto = FosterFormResultDto.of(findFosterForm);

      data.put("FosterForm", fosterFormResultDto);
    } else {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
    return SuccessResult.success(data);
  }

  //내가 보낸 입양신청서 목록조회
  @Transactional
  public Map<String, Object> getMyFosterForms(UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    }

    Long userId = userDetails.getUser().getUserId();
    User user = userRepository.getById(userId);

    List<FosterForm> fosterForms = fosterFormRepository.findAllByUser(user);
    if (fosterForms.size() == 0) {
      throw new DockingException(ErrorCode.FOSTERFORM_NOT_FOUND);
    }

    List<FosterPreviewDto> fosterPreviewDtos = new ArrayList<>();
    for (FosterForm fosterForm : fosterForms) {
      Post post = fosterForm.getPost();
      PostPreviewDto postPreviewDto = PostPreviewDto.of(post);
      FosterPreviewDto fosterPreviewDto = FosterPreviewDto.of(fosterForm, postPreviewDto);

      fosterPreviewDtos.add(fosterPreviewDto);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("fosterFormPreviewList", fosterPreviewDtos);
    return SuccessResult.success(data);
  }

  //내가 올린 post 목록조회
  @Transactional
  public Map<String, Object> getMyPosts(UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    }

    User user = userDetails.getUser();

    List<Post> myPosts = postRepository.findAllByUser(user);
    List<FormsInPostDto> formsInPostDtos = new ArrayList<>();

    for (Post post : myPosts) {
      PostPreviewDto postPreviewDto = PostPreviewDto.of(post);
      List<FormPreviewDto> formPreviewDtos = new ArrayList<>();
      List<FosterForm> fosterForms = post.getFormList();

      for (FosterForm fosterForm : fosterForms) {
        FormPreviewDto formPreviewDto = FormPreviewDto.of(fosterForm);
        formPreviewDtos.add(formPreviewDto);
      }

      FormsInPostDto formsInPostDto = FormsInPostDto.of(postPreviewDto, formPreviewDtos);
      formsInPostDtos.add(formsInPostDto);
    }
    Map<String, Object> data = new HashMap<>();
    data.put("formsInPostsPreview", formsInPostDtos);
    return SuccessResult.success(data);
  }
}

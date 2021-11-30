package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.alarm.AlarmRepository;
import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.education.Education;
import com.sparta.dockingfinalproject.education.EducationRepository;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.dto.AcceptanceRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormPreviewDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormResultDto;
import com.sparta.dockingfinalproject.fosterForm.dto.MyPostsResponseDto;
import com.sparta.dockingfinalproject.fosterForm.dto.MyRequestsDto;
import com.sparta.dockingfinalproject.fosterForm.model.Acceptance;
import com.sparta.dockingfinalproject.fosterForm.model.FosterForm;
import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import com.sparta.dockingfinalproject.post.dto.PostPreviewDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;


@Service
public class FosterFormService {

  private final FosterFormRepository fosterFormRepository;
  private final PostRepository postRepository;
  private final AlarmRepository alarmRepository;
  private final SimpMessageSendingOperations simpMessageSendingOperations;
  private final EducationRepository educationRepository;

  public FosterFormService(FosterFormRepository fosterFormRepository, PostRepository postRepository,
      AlarmRepository alarmRepository, SimpMessageSendingOperations simpMessageSendingOperations,
      EducationRepository educationRepository) {
    this.fosterFormRepository = fosterFormRepository;
    this.postRepository = postRepository;
    this.alarmRepository = alarmRepository;
    this.simpMessageSendingOperations = simpMessageSendingOperations;
    this.educationRepository = educationRepository;
  }

  @Transactional
  public Map<String, Object> addFosterForm(Long postId, FosterFormRequestDto fosterFormRequestDto,
      UserDetailsImpl userDetails) {
    User user = bringUser(userDetails);
    Post findPost = bringPost(postId);
    validateTag(findPost);
    checkDuplicateRequest(user, findPost);
    checkIsAdopted(findPost);
    checkEduStatus(user);

    Acceptance acceptance = Acceptance.valueOf("waiting");
    FosterForm fosterForm = new FosterForm(findPost, fosterFormRequestDto, user, acceptance);
    fosterFormRepository.save(fosterForm);

    saveFosterAlarm(user.getNickname(), fosterForm.getFosterFormId(), findPost.getUser());
    alarmBySocketMessage(findPost.getUser(), user.getNickname());

    Map<String, Object> data = new HashMap<>();
    data.put("msg", "입양 신청이 완료 되었습니다");
    return SuccessResult.success(data);
  }

  private void validateTag(Post findPost) {
    if (findPost.getPet().getTag() == null || !findPost.getPet().getTag().equals("직접등록")) {
      throw new DockingException(ErrorCode.NO_AVAILABILITY);
    }
  }

  private void checkDuplicateRequest(User user, Post findPost) {
    if (user.validateUser(findPost.getUser().getUserId())) {
      throw new DockingException(ErrorCode.NOT_AVAILABLE_FOR_MINE);
    } else {
      List<FosterForm> fosterForms = findPost.getFormList();
      for (FosterForm fosterform : fosterForms) {
        if (user.validateUser(bringFosterFormWriter(fosterform).getUserId())) {
          throw new DockingException(ErrorCode.REQUEST_DUPLICATE);
        }
      }
    }
  }

  private void checkIsAdopted(Post findPost) {
    if (findPost.getPet().getIsAdopted() != IsAdopted.ABANDONED) {
      throw new DockingException(ErrorCode.NO_AVAILABILITY);
    }
  }

  private void checkEduStatus(User user) {
    Education education = educationRepository.findByUser(user).orElseThrow(
        () -> new DockingException(ErrorCode.EDUCATION_NOT_FOUND)
    );
    if (!education.getBasic()) {
      throw new DockingException(ErrorCode.BASIC_EDUCATION_REQUIRED);
    }
  }

  private void saveFosterAlarm(String fosterNickname, Long fosterFormId, User user) {
    String alarmContent = fosterNickname + "님이 입양신청을 하였습니다.";
    AlarmType alarmType = AlarmType.FOSTER_FORM;
    Alarm alarm = new Alarm(alarmContent, alarmType, fosterFormId, user);
    alarmRepository.save(alarm);
  }


  @Transactional
  public Map<String, Object> getFosterForm(Long fosterFormId, UserDetailsImpl userDetails) {
    User user = bringUser(userDetails);
    FosterForm findFosterForm = bringFosterForm(fosterFormId);
    checkFosterFormAccess(user, findFosterForm);
    User fosterFormWriter = bringFosterFormWriter(findFosterForm);
    Map<String, Object> eduStatus = bringEduStatus(fosterFormWriter);

    FosterFormResultDto fosterFormResultDto = FosterFormResultDto.of(findFosterForm, eduStatus);
    Map<String, Object> data = new HashMap<>();
    data.put("fosterForm", fosterFormResultDto);
    return SuccessResult.success(data);
  }

  private void checkFosterFormAccess(User user, FosterForm findFosterForm) {
    Long postWriterId = bringPostWriter(findFosterForm).getUserId();
    Long fosterFormWriterId = bringFosterFormWriter(findFosterForm).getUserId();
    if (!user.validateUser(postWriterId) && !user.validateUser(fosterFormWriterId)) {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
  }


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

  private List<FosterForm> bringFosterForms(User user) {
    List<FosterForm> fosterForms = fosterFormRepository.findAllByUser(user);
    if (fosterForms.size() == 0) {
      throw new DockingException(ErrorCode.FOSTERFORM_NOT_FOUND);
    }
    return fosterForms;
  }


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
        User fosterFormWriter = bringFosterFormWriter(fosterForm);
        Map<String, Object> eduStatus = bringEduStatus(fosterFormWriter);
        FosterFormPreviewDto fosterFormPreviewDto = FosterFormPreviewDto.of(fosterForm, eduStatus);
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

    saveAcceptanceAlarm(newAcceptance, user.getNickname(), findFosterForm.getFosterFormId(),
        findFosterForm.getUser());
    alarmBySocketMessage(postWriter, user.getNickname());

    Map<String, String> data = new HashMap<>();
    updateNewAcceptance(findFosterForm, data, acceptance, newAcceptance);
    modifyPetAdopted(findFosterForm);
    return SuccessResult.success(data);
  }

  private void postWriterAuthority(User user, User postWriter) {
    if (!user.validateUser(postWriter.getUserId())) {
      throw new DockingException(ErrorCode.NO_AUTHORIZATION);
    }
  }

  private void saveAcceptanceAlarm(Acceptance newAcceptance, String postWriterNickname,
      Long fosterFormId, User user) {
    String alarmContent;
    if (newAcceptance == Acceptance.accepted) {
      alarmContent = postWriterNickname + "님이 입양신청을 승낙하였습니다.";
    } else {
      alarmContent = postWriterNickname + "님이 입양신청을 반려하였습니다.";
    }
    AlarmType alarmType = AlarmType.FOSTER_FORM;
    Alarm alarm = new Alarm(alarmContent, alarmType, fosterFormId, user);
    alarmRepository.save(alarm);
  }

  private void updateNewAcceptance(FosterForm findFosterForm, Map<String, String> data,
      Acceptance acceptance, Acceptance newAcceptance) {
    if (newAcceptance.equals(acceptance)) {
      throw new DockingException(ErrorCode.NO_DIFFERENCE);
    } else {
      findFosterForm.updateAcceptance(newAcceptance);
      data.put("msg", "승낙여부가 <" + newAcceptance + ">(으)로 변경되었습니다.");
    }
  }

  private void modifyPetAdopted(FosterForm fosterForm) {
    Post post = fosterForm.getPost();
    Pet pet = post.getPet();
    pet.updateStatus("adopted");
  }


  private User bringUser(UserDetailsImpl userDetails) {
    if (userDetails == null) {
      throw new DockingException(ErrorCode.USER_NOT_FOUND);
    } else {
      return userDetails.getUser();
    }
  }

  private User bringPostWriter(FosterForm findFosterForm) {
    Long postId = findFosterForm.getPost().getPostId();
    Post findPost = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
    return findPost.getUser();
  }

  private User bringFosterFormWriter(FosterForm findFosterForm) {
    return findFosterForm.getUser();
  }

  private Map<String, Object> bringEduStatus(User user) {
    Education education = educationRepository.findByUser(user).orElseThrow(
        () -> new DockingException(ErrorCode.EDUCATION_NOT_FOUND)
    );

    Map<String, Object> eduStatus = new LinkedHashMap<>();
    eduStatus.put("필수지식", education.getBasic());
    eduStatus.put("심화지식", education.getAdvanced());
    eduStatus.put("심화지식2", education.getCore());

    return eduStatus;
  }

  private Post bringPost(Long postId) {
    return postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );
  }

  private FosterForm bringFosterForm(Long fosterFormId) {
    return fosterFormRepository.findById(fosterFormId).orElseThrow(
        () -> new DockingException(ErrorCode.FOSTERFORM_NOT_FOUND)
    );
  }

  private void alarmBySocketMessage(User user, String alarmNickname) {
    List<Alarm> alarms = alarmRepository
        .findAllByUserAndCheckedTrueOrderByCreatedAtDesc(user);
    Map<String, Object> result = new HashMap<>();
    result.put("alarmCount", alarms.size() + 1);
    result.put("alarmNickname", alarmNickname);
    simpMessageSendingOperations.convertAndSend("/sub/" + user.getUserId(), result);
  }

}




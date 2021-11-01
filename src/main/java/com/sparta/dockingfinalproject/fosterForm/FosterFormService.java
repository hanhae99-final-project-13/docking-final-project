package com.sparta.dockingfinalproject.fosterForm;

import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.fosterForm.dto.FosterFormRequestDto;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import java.util.HashMap;
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
  public Map<String, Object> addFosterForm(Long postId, FosterFormRequestDto fosterFormrRequestDto,
      UserDetailsImpl userDetails) {
    //User 가져오기
    Long userId = userDetails.getUser().getUserId();
    User user = userRepository.getById(userId);

    //해당 Post 가져오기
    Post post = postRepository.findById(postId).orElseThrow(
        () -> new DockingException(ErrorCode.POST_NOT_FOUND)
    );

    //db에 FosterForm 저장
    FosterForm fosterForm = new FosterForm(post, fosterFormrRequestDto, user);
    fosterFormRepository.save(fosterForm);

    //리턴 data 생성
    Map<String, Object> data = new HashMap<>();
    data.put("msg", "입양 신청이 완료 되었습니다");
    return SuccessResult.success(data);

  }

}

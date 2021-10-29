package com.sparta.dockingfinalproject.comment;

import com.sparta.dockingfinalproject.comment.dto.CommentRequestDto;
import com.sparta.dockingfinalproject.common.SuccessResult;
import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Map<String, Object> addComment(Long postId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        //User 가져오기
        Long userId = userDetails.getUser().getUserId();
        String nickname = userDetails.getUser().getNickname();
        User user = userRepository.getById(userId);

        //해당 Post 가져오기
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new DockingException(ErrorCode.POST_NOT_FOUND)
        );

        //Comment db에 저장
        Comment comment = new Comment(post, commentRequestDto, user);
        post.getCommentList().add(comment);
        postRepository.save(post);
        commentRepository.save(comment);

        //리턴 data 생성
        Map<String, Object> data = new HashMap<>();
        data.put("msg", "댓글이 등록 되었습니다");
        return SuccessResult.success(data);

    }
}

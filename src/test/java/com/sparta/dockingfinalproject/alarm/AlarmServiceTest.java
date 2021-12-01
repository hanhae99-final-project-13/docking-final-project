package com.sparta.dockingfinalproject.alarm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.alarm.model.Alarm;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.alarm.repository.AlarmRepository;
import com.sparta.dockingfinalproject.comment.model.Comment;
import com.sparta.dockingfinalproject.comment.repository.CommentRepository;
import com.sparta.dockingfinalproject.post.model.Post;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import com.sparta.dockingfinalproject.user.User;
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

@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

  @InjectMocks
  private AlarmService alarmService;

  @Mock
  private AlarmRepository alarmRepository;

  @Mock
  private CommentRepository commentRepository;

  private UserDetailsImpl userDetails;
  private User user;
  private Alarm alarm1;
  private Alarm alarm2;
  private Alarm alarm3;
  private Comment comment;
  private Post post;

  @BeforeEach
  void init() {
    user = new User(1L, "user1", "aa1234", "홍길동", "sss@naver.com", "", "",  0L, "");
    userDetails = new UserDetailsImpl(user);

    post = new Post(100L, 0L, null, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    comment = new Comment(503L, "댓글test", post, user);

    alarm1 = new Alarm(10L, "", false, AlarmType.SIGN_UP, 501L, user);
    alarm2 = new Alarm(20L, "", false, AlarmType.SIGN_UP, 502L, user);
    alarm3 = new Alarm(30L, "", false, AlarmType.COMMENT, 503L, user);
  }

  @Test
  @DisplayName("회원별 알람 리스트 조회")
  void getUserAlarms() {
    List<AlarmResponseDto> alarmResponseDtos = new ArrayList<>();
    alarmResponseDtos.add(new AlarmResponseDto(alarm1.getAlarmId(), alarm1.getAlarmContent(), alarm1.isChecked(), alarm1.getAlarmType(),
        alarm1.getContentId(), null, null, alarm1.getCreatedAt()));

    alarmResponseDtos.add(new AlarmResponseDto(alarm2.getAlarmId(), alarm2.getAlarmContent(), alarm2.isChecked(), alarm2.getAlarmType(),
        alarm2.getContentId(), null, null, alarm2.getCreatedAt()));

    alarmResponseDtos.add(new AlarmResponseDto(alarm3.getAlarmId(), alarm3.getAlarmContent(), alarm3.isChecked(), alarm3.getAlarmType(),
        alarm3.getContentId(), comment.getPost().getPostId(), comment.getComment(), alarm3.getCreatedAt()));

    List<Alarm> alarms = new ArrayList<>();
    alarms.add(alarm1);
    alarms.add(alarm2);
    alarms.add(alarm3);

    when(alarmRepository.findAllByUserAndCheckedTrueOrderByCreatedAtDesc(user)).thenReturn(alarms);
    when(alarmRepository.findAllUserAlarm(user)).thenReturn(alarmResponseDtos);

    Map<String, Object> data = (Map<String, Object>) alarmService.getAlarms(user).get("data");
    List<AlarmResponseDto> findAlarms = (List<AlarmResponseDto>) data.get("data");

    assertThat(data.get("alarmCount")).isEqualTo(3);
    assertThat(findAlarms.get(0).getAlarmId()).isEqualTo(10L);
    assertThat(findAlarms.get(1).getAlarmId()).isEqualTo(20L);
//    assertThat(findAlarms.get(2).getComment()).isEqualTo("댓글test");
    assertThat(findAlarms.get(2).getPostId()).isEqualTo(100L);
  }

  @Test
  @DisplayName("알람 단건 상세 조회")
  void getAlarm() {
    AlarmResponseDto alarmResponseDto1 = new AlarmResponseDto(alarm1.getAlarmId(),
        alarm1.getAlarmContent(), alarm1.isChecked(), alarm1.getAlarmType(),
        alarm1.getContentId(), null, null, alarm1.getCreatedAt());
    when(alarmRepository.findUserAlarm(10L)).thenReturn(Optional.of(alarmResponseDto1));
    when(alarmRepository.findById(10L)).thenReturn(Optional.of(alarm1));

    Map<String, Object> alarm = (Map<String, Object>) alarmService.getAlarm(10L, user).get("data");
    AlarmResponseDto alarmResponseDto = (AlarmResponseDto) alarm.get("data");

    assertThat(alarmResponseDto.getAlarmId()).isEqualTo(10L);
  }

  @Test
  @DisplayName("알람 단건 조회시 상태 false 변환")
  void modifyAlarmStatus() {
    AlarmResponseDto alarmResponseDto1 = new AlarmResponseDto(alarm1.getAlarmId(),
        alarm1.getAlarmContent(), alarm1.isChecked(), alarm1.getAlarmType(),
        alarm1.getContentId(), null, null, alarm1.getCreatedAt());
    when(alarmRepository.findUserAlarm(10L)).thenReturn(Optional.of(alarmResponseDto1));
    when(alarmRepository.findById(10L)).thenReturn(Optional.of(alarm1));

    Map<String, Object> alarm = (Map<String, Object>) alarmService.getAlarm(10L, user).get("data");

    assertThat(alarmRepository.findById(10L).get().isChecked()).isFalse();
  }

  @Test
  @DisplayName("알람 전체 삭제")
  void deleteAllAlarms() {

  }
}
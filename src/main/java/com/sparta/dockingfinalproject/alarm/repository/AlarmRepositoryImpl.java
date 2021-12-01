package com.sparta.dockingfinalproject.alarm.repository;

import static com.sparta.dockingfinalproject.alarm.model.QAlarm.alarm;
import static com.sparta.dockingfinalproject.comment.model.QComment.comment1;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dockingfinalproject.alarm.dto.AlarmResponseDto;
import com.sparta.dockingfinalproject.alarm.dto.QAlarmResponseDto;
import com.sparta.dockingfinalproject.alarm.model.AlarmType;
import com.sparta.dockingfinalproject.user.User;
import java.util.List;
import javax.persistence.EntityManager;

public class AlarmRepositoryImpl implements AlarmRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public AlarmRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<AlarmResponseDto> findAllUserAlarm(User user) {
    List<AlarmResponseDto> alarmResponseDtos = queryFactory
        .select(new QAlarmResponseDto(
            alarm.alarmId,
            alarm.alarmContent,
            alarm.checked,
            alarm.alarmType,
            alarm.contentId,
            alarm.createdAt
        ))
        .from(alarm)
        .where(alarm.user.userId.eq(user.getUserId()))
        .orderBy(alarm.createdAt.desc())
        .fetch();

    for (AlarmResponseDto alarmResponseDto : alarmResponseDtos) {
      System.out.println(alarmResponseDto.getAlarmType());
      if (alarmResponseDto.getAlarmType() == AlarmType.COMMENT) {
        System.out.println("실행");
        Tuple commentTuple = queryFactory
            .select(
                comment1.post.postId,
                comment1.comment
            )
            .from(comment1)
            .where(comment1.commentId.eq(alarmResponseDto.getContentId()))
            .fetchOne();

        String comment = commentTuple.get(comment1.comment);
        Long postId = commentTuple.get(comment1.post.postId);
        alarmResponseDto.addComment(comment, postId);
      }
    }
    return alarmResponseDtos;
  }

}

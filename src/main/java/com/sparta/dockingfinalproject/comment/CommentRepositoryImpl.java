package com.sparta.dockingfinalproject.comment;

import static com.sparta.dockingfinalproject.comment.QComment.comment1;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dockingfinalproject.comment.dto.CommentResultDto;
import com.sparta.dockingfinalproject.comment.dto.QCommentResultDto;
import com.sparta.dockingfinalproject.post.Post;
import java.util.List;
import javax.persistence.EntityManager;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public CommentRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<CommentResultDto> findAllPostInComment(Post post) {
    return queryFactory
        .select(new QCommentResultDto(
            comment1.commentId,
            comment1.comment,
            comment1.user.nickname,
            comment1.user.userImgUrl,
            comment1.createdAt,
            comment1.modifiedAt
        ))
        .from(comment1)
        .where(comment1.post.postId.eq(post.getPostId()))
        .orderBy(comment1.createdAt.desc())
        .fetch();
  }
}

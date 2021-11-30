package com.sparta.dockingfinalproject.post.repository;


import static com.sparta.dockingfinalproject.post.model.QPost.post;
import static com.sparta.dockingfinalproject.wish.QWish.wish;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dockingfinalproject.post.dto.PostDetailResponseDto;
import com.sparta.dockingfinalproject.post.dto.PostPreviewDto;
import com.sparta.dockingfinalproject.post.dto.PostSearchRequestDto;
import com.sparta.dockingfinalproject.post.dto.PostSearchResponseDto;
import com.sparta.dockingfinalproject.post.dto.QPostDetailResponseDto;
import com.sparta.dockingfinalproject.post.dto.QPostPreviewDto;
import com.sparta.dockingfinalproject.post.dto.QPostSearchResponseDto;
import com.sparta.dockingfinalproject.security.UserDetailsImpl;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PostRepositoryImpl implements PostRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public PostRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<PostSearchResponseDto> searchPagePost(Pageable pageable, PostSearchRequestDto postSearchRequestDto) {
    QueryResults<PostSearchResponseDto> results = queryFactory
        .select(new QPostSearchResponseDto(
            post.user.userId,
            post.user.nickname,
            post.postId,
            post.pet.breed,
            post.pet.sex,
            post.pet.age,
            post.pet.ownerType,
            post.pet.address,
            post.pet.img.as("imgs"),
            post.pet.isAdopted,
            post.pet.createdAt,
            post.pet.modifiedAt
        ))
        .from(post)
        .where(
            startDtBetween(postSearchRequestDto.getStartDt(), postSearchRequestDto.getEndDt()),
            ownerTypeContains(postSearchRequestDto.getOwnerType()),
            addressEq(postSearchRequestDto.getCity(), postSearchRequestDto.getDistrict())
        )
        .orderBy(sortCreateAt(postSearchRequestDto.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<PostSearchResponseDto> content = results.getResults();
    long total = results.getTotal();
    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression startDtBetween(String startDt, String endDt) {
    if (hasText(startDt) && hasText(endDt)) {
      LocalDateTime start = getStartDt(startDt);
      LocalDateTime end = getEndDt(endDt);
      return post.pet.createdAt.between(start, end);
    }
    return null;
  }

  private LocalDateTime getStartDt(String startDt) {
    if (hasText(startDt)) {
      String[] starts = startDt.trim().split("-");
      return LocalDateTime.of(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]), 0, 0);
    }
    return null;
  }

  private LocalDateTime getEndDt(String endDt) {
    if (hasText(endDt)) {
      String[] ends = endDt.trim().split("-");
      return LocalDateTime.of(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]), 23,59);
    }
    return null;
  }

  private BooleanExpression ownerTypeContains(String ownerType) {
    return hasText(ownerType) ? post.pet.ownerType.trim().contains(ownerType) : null;
  }

  private BooleanExpression addressEq(String city, String district) {
    if (hasText(city) && hasText(district)) {
      String address = city.trim() + " " + district.trim();
      return post.pet.address.eq(address);
    }

    if (hasText(city)) {
      return post.pet.address.startsWith(city.trim());
    }
    return null;
  }

  private OrderSpecifier<?> sortCreateAt(String sort) {
    if (sort.trim().equalsIgnoreCase("new")) {
      return post.pet.createdAt.desc();
    }
    return post.pet.createdAt.asc();
  }

  @Override
  public List<PostPreviewDto> findHomePosts(Pageable pageable) {
    return queryFactory
        .select(new QPostPreviewDto(
            post.postId,
            post.pet.createdAt,
            post.pet.modifiedAt,
            post.pet.breed,
            post.pet.sex,
            post.pet.age,
            post.pet.ownerType,
            post.pet.address,
            post.pet.img.as("imgs"),
            post.pet.isAdopted
        ))
        .from(post)
        .orderBy(post.pet.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  @Override
  public PostDetailResponseDto findPostDetail(Long postId, UserDetailsImpl userDetails) {
    PostDetailResponseDto postDetailResponseDto = queryFactory
        .select(new QPostDetailResponseDto(
            post.user.userId,
            post.user.nickname,
            post.postId,
            post.pet.breed,
            post.pet.sex,
            post.pet.age,
            post.pet.weight,
            post.pet.lostLocation,
            post.pet.ownerType,
            post.pet.phone,
            post.pet.address,
            post.pet.tag,
            post.pet.url,
            post.pet.img.as("imgs"),
            post.pet.extra,
            post.pet.isAdopted,
            post.pet.createdAt,
            post.pet.modifiedAt

        ))
        .from(post)
        .where(
            post.postId.eq(postId)
        )
        .fetchOne();

    Long wishId = null;
    if (userDetails != null) {
      wishId = queryFactory
          .select(wish.wishId)
          .from(wish)
          .where(
              wish.post.postId.eq(postId),
              wish.user.userId.eq(userDetails.getUser().getUserId())
          ).fetchOne();
    }

    if (wishId != null) {
      postDetailResponseDto.addHeart(true);
    }
    return postDetailResponseDto;
  }

  private BooleanExpression wishEqUser(Long postId, UserDetailsImpl userDetails) {
    return userDetails != null ? wish.post.postId.eq(postId)
        .and(wish.user.userId.eq(userDetails.getUser().getUserId())) : null;
  }
}
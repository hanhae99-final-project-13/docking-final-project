package com.sparta.dockingfinalproject.post;

import static com.sparta.dockingfinalproject.post.QPost.post;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dockingfinalproject.post.dto.PostSearchRequestDto;
import com.sparta.dockingfinalproject.post.dto.PostSearchResponseDto;
import com.sparta.dockingfinalproject.post.dto.QPostSearchResponseDto;
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
    String[] starts = startDt.split("-");
    return LocalDateTime.of(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]), 0, 0);
  }

  private LocalDateTime getEndDt(String endDt) {
    String[] ends = endDt.split("-");
    return LocalDateTime.of(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]), 23,59);
  }

  private BooleanExpression ownerTypeContains(String ownerType) {
    return hasText(ownerType) ? post.pet.ownerType.contains(ownerType) : null;
  }

  private BooleanExpression addressEq(String city, String district) {
    if (hasText(city) && hasText(district)) {
      String address = city + " " + district;
      return post.pet.address.eq(address);
    }
    return null;
  }

  private OrderSpecifier<?> sortCreateAt(String sort) {
    if (sort.equalsIgnoreCase("new")) {
      return post.pet.createdAt.desc();
    }
    return post.pet.createdAt.asc();
  }
}

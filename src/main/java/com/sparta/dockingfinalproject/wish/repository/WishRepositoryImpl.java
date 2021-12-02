package com.sparta.dockingfinalproject.wish.repository;

import static com.sparta.dockingfinalproject.wish.model.QWish.wish;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dockingfinalproject.user.model.User;
import com.sparta.dockingfinalproject.wish.dto.QWishResultDto;
import com.sparta.dockingfinalproject.wish.dto.WishResultDto;
import java.util.List;
import javax.persistence.EntityManager;

public class WishRepositoryImpl implements WishRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public WishRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<WishResultDto> findAllMyWishs(User user) {
    return queryFactory
        .select(new QWishResultDto(
            wish.wishId,
            wish.post.postId,
            wish.post.createdAt,
            wish.post.modifiedAt,
            wish.post.pet.breed,
            wish.post.pet.sex,
            wish.post.pet.age,
            wish.post.pet.ownerType,
            wish.post.pet.address,
            wish.post.pet.img.as("imgs"),
            wish.post.pet.isAdopted
        ))
        .from(wish)
        .where(wish.user.userId.eq(user.getUserId()))
        .fetch();
  }
}

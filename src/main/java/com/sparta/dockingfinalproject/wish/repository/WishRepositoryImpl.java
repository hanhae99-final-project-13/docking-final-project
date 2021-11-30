package com.sparta.dockingfinalproject.wish.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dockingfinalproject.user.User;
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
    return null;
  }
}

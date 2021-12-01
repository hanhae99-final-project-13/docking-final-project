package com.sparta.dockingfinalproject.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dockingfinalproject.fosterForm.model.QFosterForm;
import java.util.List;
import javax.persistence.EntityManager;

public class UserRepositoryImpl implements  UserRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  public UserRepositoryImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em);}

  public List<Long> getPostIdFromFosterForm(User user) {
	QFosterForm qFosterForm = QFosterForm.fosterForm;
	List<Long> requestedPostIdList = queryFactory.select(qFosterForm.post.postId).from(qFosterForm)
		.where(qFosterForm.user.eq(user)).fetch();

	return requestedPostIdList;
  }

}

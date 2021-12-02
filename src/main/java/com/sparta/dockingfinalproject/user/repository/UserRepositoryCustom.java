package com.sparta.dockingfinalproject.user.repository;

import com.sparta.dockingfinalproject.user.model.User;
import java.util.List;

public interface UserRepositoryCustom {

  List<Long> getPostIdFromFosterForm(User user);
}

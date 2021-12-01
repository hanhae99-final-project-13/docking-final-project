package com.sparta.dockingfinalproject.user;

import java.util.List;

public interface UserRepositoryCustom {

  List<Long> getPostIdFromFosterForm(User user);
}

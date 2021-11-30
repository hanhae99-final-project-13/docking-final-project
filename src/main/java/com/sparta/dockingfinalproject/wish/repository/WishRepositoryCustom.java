package com.sparta.dockingfinalproject.wish.repository;

import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.wish.dto.WishResultDto;
import java.util.List;

public interface WishRepositoryCustom {

  List<WishResultDto> findAllMyWishs(User user);
}

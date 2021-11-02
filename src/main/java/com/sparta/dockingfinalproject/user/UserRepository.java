package com.sparta.dockingfinalproject.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
    Optional<User>  findAllByAuthCheckTrueAndUsername(String username);



}

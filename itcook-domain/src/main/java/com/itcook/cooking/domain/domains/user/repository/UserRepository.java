package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;

import java.util.List;
import java.util.Optional;

import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<ItCookUser, Long> {

    @Query("select u from  ItCookUser u where  u.email = :email and u.userState = com.itcook.cooking.domain.domains.user.enums.UserState.ACTIVE")
    Optional<ItCookUser> findByEmail(String email);
    @Query("select u from  ItCookUser u where  u.id = :userId and u.userState = com.itcook.cooking.domain.domains.user.enums.UserState.DELETE")
    Optional<ItCookUser> findDeleteUserById(Long userId);

    @Override
    @Query("select u from ItCookUser u where  u.id = :userId and u.userState = com.itcook.cooking.domain.domains.user.enums.UserState.ACTIVE")
    Optional<ItCookUser> findById(Long userId);

    Optional<ItCookUser> findByNickName(String nickName);
}

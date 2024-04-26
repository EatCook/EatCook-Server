package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;

import com.itcook.cooking.domain.domains.user.enums.UserState;
import java.util.List;
import java.util.Optional;

import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<ItCookUser, Long> {

    Optional<ItCookUser> findByEmail(String email);
    Optional<ItCookUser> findByEmailAndUserState(String email, UserState userState);
    Optional<ItCookUser> findByNickName(String nickName);
}

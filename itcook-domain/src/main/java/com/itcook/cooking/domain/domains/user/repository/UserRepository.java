package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;

import java.util.List;
import java.util.Optional;

import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ItCookUser, Long> {

    Optional<ItCookUser> findByEmail(String email);

    List<CookTalkUserMapping> findByIdIn(List<Long> id);

    Optional<ItCookUser> findByNickName(String nickName);
}

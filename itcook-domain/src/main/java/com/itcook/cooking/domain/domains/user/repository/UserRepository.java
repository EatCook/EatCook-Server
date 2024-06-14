package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.UserState;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ItCookUser, Long> {

    Optional<ItCookUser> findByEmail(String email);
    Optional<ItCookUser> findByEmailAndUserState(String email, UserState userState);
    Optional<ItCookUser> findByNickName(String nickName);
}

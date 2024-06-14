package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.UserState;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<ItCookUser, Long> {

    Optional<ItCookUser> findByEmail(String email);
    Optional<ItCookUser> findByEmailAndUserState(String email, UserState userState);
    Optional<ItCookUser> findByNickName(String nickName);

    @Query("select distinct u from ItCookUser u left join fetch u.userCookingThemes uc where u.email = :email")
    ItCookUser findItCookUserJoinCookingThemes(String email); // 단일 조회하므로 distinct join fetch 사용
}

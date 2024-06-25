package com.itcook.cooking.domain.domains.user.domain.repository;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.entity.UserCookingTheme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCookingThemeRepository extends JpaRepository<UserCookingTheme, Long> {

    @Modifying
    @Query("delete from UserCookingTheme uc where uc.user = :user")
    void deleteByUser(ItCookUser user);

    @Query("select uc from UserCookingTheme uc where uc.user.id = :userId")
    List<UserCookingTheme> findByUserId(Long userId);

}

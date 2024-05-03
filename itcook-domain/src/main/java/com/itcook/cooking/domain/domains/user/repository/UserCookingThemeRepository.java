package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCookingThemeRepository extends JpaRepository<UserCookingTheme, Long> {

    List<UserCookingTheme> findAllByUserId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from UserCookingTheme uc where uc.userId = :userId")
    void deleteAllByUserId(Long userId);

}

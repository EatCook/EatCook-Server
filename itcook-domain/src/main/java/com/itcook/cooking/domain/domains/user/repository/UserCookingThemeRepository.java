package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCookingThemeRepository extends JpaRepository<UserCookingTheme, Long> {

    List<UserCookingTheme> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);

}

package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserCookingThemeDomainService {
    private final UserCookingThemeRepository userCookingThemeRepository;

    public List<UserCookingTheme> getUserCookingTheme(Long userId) {
        return userCookingThemeRepository.findAllByUserId(userId);
    }
}

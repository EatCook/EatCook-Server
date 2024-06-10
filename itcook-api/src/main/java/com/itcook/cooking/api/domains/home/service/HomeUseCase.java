package com.itcook.cooking.api.domains.home.service;

import com.itcook.cooking.api.domains.home.dto.response.HomeReadResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.service.UserCookingThemeDomainService;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeUseCase {

    private final UserDomainService userDomainService;
    private final UserCookingThemeDomainService userCookingThemeDomainService;

    public HomeReadResponse getHomePage(String username) {
        ItCookUser findItCookUserData = userDomainService.findUserByEmail(username);
        List<UserCookingTheme> findUserCookingTheme = userCookingThemeDomainService.getUserCookingTheme(findItCookUserData.getId());

        return HomeReadResponse.of(
                findItCookUserData.getNickName(),
                findUserCookingTheme
                );
    }
}

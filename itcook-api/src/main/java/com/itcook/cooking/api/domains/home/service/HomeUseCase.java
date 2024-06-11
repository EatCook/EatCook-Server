package com.itcook.cooking.api.domains.home.service;

import com.itcook.cooking.api.domains.home.dto.response.HomePagingInterestReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomePagingLifeTypeReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomeUserCookingThemeReadResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.PostCookingThemeErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.repository.dto.HomePostDto;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.service.UserCookingThemeDomainService;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeUseCase {

    private final UserDomainService userDomainService;
    private final UserCookingThemeDomainService userCookingThemeDomainService;
    private final PostDomainService postDomainService;

    public HomeUserCookingThemeReadResponse getHomePage(String username) {
        ItCookUser findItCookUserData = userDomainService.findUserByEmail(username);
        List<UserCookingTheme> findUserCookingTheme = userCookingThemeDomainService.getUserCookingTheme(findItCookUserData.getId());

        return HomeUserCookingThemeReadResponse.of(
                findItCookUserData.getNickName(),
                findUserCookingTheme
        );
    }

    public HomePagingInterestReadResponse getPostByCookingTheme(String cookingTheme, String username, Pageable pageable) {
        ItCookUser findItCookUserData = userDomainService.findUserByEmail(username);
        List<UserCookingTheme> findUserCookingTheme = userCookingThemeDomainService.getUserCookingTheme(findItCookUserData.getId());

        UserCookingTheme userCookingTheme = findUserCookingTheme.stream()
                .filter(uct -> uct.getCookingType().getCookingTypeName().equals(cookingTheme))
                .findAny()
                .orElseThrow(() -> {
                    log.error("미등록 관심 요리");
                    return new ApiException(PostCookingThemeErrorCode.POST_COOKING_THEME_NOT_FOUND);
                });

        Page<HomePostDto> postsWithLikedAndArchiveDto = postDomainService.fetchFindPostsWithLikedAndArchiveDtoByCookingTheme(userCookingTheme.getCookingType(), findItCookUserData.getId(), pageable);
        return HomePagingInterestReadResponse.of(
                postsWithLikedAndArchiveDto.getContent(),
                postsWithLikedAndArchiveDto.hasNext(),
                postsWithLikedAndArchiveDto.getTotalElements(),
                postsWithLikedAndArchiveDto.getTotalPages()
        );
    }

}

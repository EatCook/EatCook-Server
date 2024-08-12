package com.itcook.cooking.api.domains.home.service;

import com.itcook.cooking.api.domains.home.dto.response.HomePagingInterestReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomePagingSpacialReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomeUserCookingThemeReadResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.LifeTypeErrorCode;
import com.itcook.cooking.domain.common.errorcode.PostCookingThemeErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeInterestDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeSpecialDto;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeUseCase {

    private final UserService userService;
    private final PostService postService;

    public HomeUserCookingThemeReadResponse getHomePage(String username) {
        ItCookUser findItCookUserData = userService.findUserByEmail(username);

        return HomeUserCookingThemeReadResponse.of(findItCookUserData);
    }

    public HomePagingInterestReadResponse getPostByCookingTheme(String cookingTheme,
            String username, Pageable pageable) {
        ItCookUser findItCookUserData = userService.findUserByEmail(username);

        UserCookingTheme userCookingTheme = findItCookUserData.getUserCookingThemes().stream()
                .filter(uct -> uct.getCookingType().toString().equals(cookingTheme))
                .findAny()
                .orElseThrow(() -> {
                    log.error("미등록 관심 요리 Request cookingTheme : {}", cookingTheme);
                    return new ApiException(PostCookingThemeErrorCode.POST_COOKING_THEME_NOT_FOUND);
                });

        Page<HomeInterestDto> postsByCookingTypeData = postService.fetchFindPostsWithLikedAndArchiveDtoByCookingTheme(
                userCookingTheme.getCookingType(), findItCookUserData.getId(), pageable
        );
        return HomePagingInterestReadResponse.of(postsByCookingTypeData);
    }

    public HomePagingSpacialReadResponse getLifeTypeByPost(String lifeType, String username,
            Pageable pageable) {
        ItCookUser findItCookUserData = userService.findUserByEmail(username);

        LifeType findLifeType = LifeType.getByLifeType(lifeType);
        if (findLifeType == null) {
            log.error("잘못된 생활 유형 요청 Request lifeType : {} ", lifeType);
            throw new ApiException(LifeTypeErrorCode.LIFETYPE_NOT_FOUND);
        }

        Page<HomeSpecialDto> postsByLifeTypeData = postService.fetchFindPostsWithLikedAndArchiveDtoByLifeTypeDefaultHealthDiet(
                findLifeType, findItCookUserData.getId(), pageable
        );
        return HomePagingSpacialReadResponse.of(postsByLifeTypeData);
    }

}

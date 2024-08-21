package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.response.CookTalkFeedsResponse;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkFollowsResponse;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.like.service.LikedService;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFollowDto;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CookTalkUseCase {

    private final PostService postService;
    private final UserService userService;

    /**
     * CookTalk 피드 조회
     */
    public PageResponse<CookTalkFeedsResponse> getCookTalkFeeds(String email, Pageable pageable) {
        ItCookUser findByUserEmail = userService.findUserByEmail(email);

        Page<CookTalkFeedDto> cookTalkFeeds = postService
                .getCookTalkFeeds(findByUserEmail.getId(), pageable);

        Page<CookTalkFeedsResponse> cookTalkResponse = CookTalkFeedsResponse
                .fromCookTalkFeedDto(findByUserEmail.getId(), cookTalkFeeds, findByUserEmail);

        return PageResponse.of(cookTalkResponse);
    }

    /**
     * CookTalk 팔로우 조회
     *
     * @return
     */
    public PageResponse<CookTalkFollowsResponse> getCookTalkFollows(String email, Pageable pageable) {
        ItCookUser findByUserEmail = userService.findUserByEmail(email);

        if (findByUserEmail.getFollow().isEmpty()) {
            log.error("팔로우 정보 없음 Email : {}", email);
            throw new ApiException(UserErrorCode.NO_FOLLOWERS);
        }

        Page<CookTalkFollowDto> cookTalkFollows = postService
                .getCookTalkFollows(findByUserEmail.getId(), findByUserEmail.getFollow(), pageable);

        Page<CookTalkFollowsResponse> cookTalkResponse = CookTalkFollowsResponse
                .fromCookTalkFeedDto(cookTalkFollows, findByUserEmail);

        return PageResponse.of(cookTalkResponse);
    }

}
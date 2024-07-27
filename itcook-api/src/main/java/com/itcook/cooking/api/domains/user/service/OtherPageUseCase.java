package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeInterestDto;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPagePostInfoResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPageUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
@RequiredArgsConstructor
public class OtherPageUseCase {

    private final UserService userService;
    private final PostService postService;

    public OtherPageUserInfoResponse getOtherPageUserInfo(String email, Long otherUserId) {
        return userService.getOtherPageInfo(email, otherUserId);
    }

    public PageResponse<OtherPagePostInfoResponse> getOtherPagePostInfo(String email, Long otherUserId,
            Pageable pageable) {
        ItCookUser authUser = userService.findUserByEmail(email);
        Page<OtherPagePostInfoResponse> otherPageInfo = postService.getOtherPageInfo(
                authUser, otherUserId, pageable);
        return PageResponse.of(otherPageInfo);
    }
}

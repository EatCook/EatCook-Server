package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.user.service.UserService;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPageUserInfoResponse;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class OtherPageUseCase {

    private final UserService userService;

    public OtherPageUserInfoResponse getOtherPageUserInfo(String email, Long otherUserId) {
        return userService.getOtherPageInfo(email, otherUserId);
    }
}

package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.FollowRequest;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@UseCase
@RequiredArgsConstructor
public class FollowUseCase {

    private final UserService userService;

    @Transactional
    public void followUser(String fromUserEmail, FollowRequest followRequest) {
        userService.follow(fromUserEmail, followRequest.getToUserId());
    }

    @Transactional
    public void unFollowUser(String fromUserEmail, Long toUserId) {
        userService.unFollow(fromUserEmail, toUserId);
    }

}

package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.follow.FollowRequest;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.events.Events;
import com.itcook.cooking.domain.common.events.user.UserFollowedEvent;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@UseCase
@RequiredArgsConstructor
public class FollowUseCase {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void toUserFollowAdd(String fromUserEmail, FollowRequest followRequest) {
        ItCookUser fromItCookUserData = userService.findUserByEmail(fromUserEmail);
        ItCookUser toItCookUserData = userService.fetchFindByUserId(followRequest.getToUserId());

        List<Long> follow = fromItCookUserData.getFollow();
        boolean contains = follow.contains(followRequest.getToUserId());

        if (contains) {
            throw new ApiException(UserErrorCode.ALREADY_FOLLOW_USER);
        }

        follow.add(toItCookUserData.getId());

        fromItCookUserData.updateFollow(follow);

        Events.raise(UserFollowedEvent.of(fromItCookUserData.getId(),fromItCookUserData.getNickName(),
            followRequest.getToUserId()));
    }

    @Transactional
    public void toUserFollowDelete(String fromUserEmail, FollowRequest followRequest) {
        ItCookUser fromItCookUserData = userService.findUserByEmail(fromUserEmail);
        ItCookUser toItCookUserData = userService.fetchFindByUserId(followRequest.getToUserId());

        List<Long> follow = fromItCookUserData.getFollow();
        boolean contains = follow.contains(followRequest.getToUserId());

        if (!contains) {
            throw new ApiException(UserErrorCode.ALREADY_FOLLOW_USER);
        }

        follow.remove(toItCookUserData.getId());

        fromItCookUserData.updateFollow(follow);
    }

}

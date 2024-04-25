package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.follow.FollowRequest;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@UseCase
@RequiredArgsConstructor
public class FollowUseCase {

    private final UserDomainService userDomainService;

    @Transactional
    public void toUserFollowAdd(String fromUserEmail, FollowRequest followRequest) {
        ItCookUser fromItCookUserData = userDomainService.findUserByEmail(fromUserEmail);
        ItCookUser toItCookUserData = userDomainService.fetchFindByUserId(followRequest.getToUserId());

        List<Long> follow = fromItCookUserData.getFollow();
        boolean contains = follow.contains(followRequest.getToUserId());

        if (contains) {
            throw new ApiException(UserErrorCode.ALREADY_FOLLOW_USER);
        }

        follow.add(toItCookUserData.getId());

        fromItCookUserData.updateFollow(follow);
    }

    @Transactional
    public void toUserFollowDelete(String fromUserEmail, FollowRequest followRequest) {
        ItCookUser fromItCookUserData = userDomainService.findUserByEmail(fromUserEmail);
        ItCookUser toItCookUserData = userDomainService.fetchFindByUserId(followRequest.getToUserId());

        List<Long> follow = fromItCookUserData.getFollow();
        boolean contains = follow.contains(followRequest.getToUserId());

        if (!contains) {
            throw new ApiException(UserErrorCode.ALREADY_FOLLOW_USER);
        }

        follow.remove(toItCookUserData.getId());

        fromItCookUserData.updateFollow(follow);
    }

}

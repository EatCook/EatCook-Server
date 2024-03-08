package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.CookTalkDto;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CookTalkUseCase {

    private final PostDomainService postDomainService;
    private final UserDomainService userDomainService;
    private final PostValidationUseCase postValidationUseCase;

    public List<CookTalkResponse> getCookTalkFeed(String email) {
        // 사용자 정보 조회
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(email);

        // 본인을 제외한 사용자 게시글 조회
        List<Post> postAllData = postDomainService.fetchFindAllByUserIdNotWithUsers(findByUserEmail.getId());

        // 유저 정보 매핑 및 팔로우 여부 설정
        return getCookTalkResponse(postAllData, findByUserEmail);
    }

    public List<CookTalkResponse> getFollowingTalk(String email) {
        // 유저 정보 검색
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(email);

        // 게시글 정보 검색
        List<Post> postFollowingData = postDomainService.fetchFindFollowingCookTalk(findByUserEmail.getFollow());

        // 유저 정보 매핑 및 팔로우 여부 설정
        return getCookTalkResponse(postFollowingData, findByUserEmail);
    }

    private List<CookTalkResponse> getCookTalkResponse(List<Post> postFollowingData, ItCookUser findByUserEmail) {
        List<CookTalkDto> cookTalkDtos = postValidationUseCase.postUserMatchingValidation(postFollowingData, CookTalkDto::new);

        Set<Long> followingSet = new HashSet<>(findByUserEmail.getFollow());

        cookTalkDtos.forEach(cookTalkDto -> postValidationUseCase.getFollowingCheck(cookTalkDto, followingSet));

        // 변환 및 반환
        return cookTalkDtos.stream()
                .map(CookTalkResponse::of)
                .collect(Collectors.toList());
    }


}
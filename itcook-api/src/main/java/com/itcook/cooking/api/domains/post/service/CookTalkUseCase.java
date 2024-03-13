package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.cooktalk.CookTalkDto;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CookTalkUseCase {

    private final PostDomainService postDomainService;
    private final UserDomainService userDomainService;
    private final PostValidationUseCase postValidationUseCase;

    public CookTalkResponse getCookTalkFeed(String email, Pageable pageable) {
        // 사용자 정보 조회
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(email);

        // 본인을 제외한 사용자 게시글 조회
        Page<Post> postAllData = postDomainService.fetchFindAllByUserIdNotWithUsers(findByUserEmail.getId(), pageable);

        // 유저 정보 매핑 및 팔로우 여부 설정
        return getCookTalkResponse(postAllData, findByUserEmail);
    }

    public CookTalkResponse getFollowingTalk(String email, Pageable pageable) {
        // 유저 정보 검색
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(email);

        // 게시글 정보 검색
        Page<Post> postFollowingData = postDomainService.fetchFindFollowingCookTalk(findByUserEmail.getFollow(), pageable);

        // 유저 정보 매핑 및 팔로우 여부 설정
        return getCookTalkResponse(postFollowingData, findByUserEmail);
    }

    private CookTalkResponse getCookTalkResponse(Page<Post> postFollowingData, ItCookUser findByUserEmail) {
        List<CookTalkDto> cookTalkDtos = postValidationUseCase.postUserMatchingValidation(postFollowingData.stream().toList(), CookTalkDto::new);

        Set<Long> followingSet = new HashSet<>(findByUserEmail.getFollow());

        cookTalkDtos.forEach(cookTalkDto -> postValidationUseCase.getFollowingCheck(cookTalkDto, followingSet));

        return CookTalkResponse.of(cookTalkDtos, postFollowingData.hasNext(), postFollowingData.getTotalElements());
    }


}
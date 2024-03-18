package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.cooktalk.CookTalkDto;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.LikedDomainService;
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
    private final LikedDomainService likedDomainService;
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

        List<Long> postIdData = postFollowingData.stream().map(Post::getId).toList();


        List<Liked> findAllLiked = likedDomainService.getFindAllLiked(postIdData);

        // postIdData를 반복하여 처리합니다.
        postIdData.forEach(postIdDatum -> {
            // postIdDatum에 해당하는 좋아요 리스트만 필터링합니다.
            List<Liked> filteredLiked = findAllLiked.stream()
                    .filter(liked -> postIdDatum.equals(liked.getPostId()))
                    .toList();

            // 해당 postIdDatum에 대한 좋아요 유효성을 확인하고, cookTalkDto의 상태를 설정합니다.
            cookTalkDtos.stream()
                    .filter(cookTalkDto -> Objects.equals(cookTalkDto.getId(), postIdDatum))
                    .findFirst()
                    .ifPresent(cookTalkDto -> cookTalkDto.of(filteredLiked, postValidationUseCase.getLikedValidation(filteredLiked, findByUserEmail.getId())));
        });


        return CookTalkResponse.of(cookTalkDtos, postFollowingData.hasNext(), postFollowingData.getTotalElements(), postFollowingData.getTotalPages());
    }


}
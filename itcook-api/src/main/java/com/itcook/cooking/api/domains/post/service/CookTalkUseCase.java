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
import java.util.stream.Collectors;


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
        ItCookUser findByUserEmail = userDomainService.findUserByEmail(email);

        Page<Object[]> findAllPostAndUserData = postDomainService.fetchFindAllByCookTalkFeedV2(findByUserEmail.getId(), pageable);

        return getCookTalkResponse(findAllPostAndUserData, findByUserEmail);
    }

    public CookTalkResponse getFollowingTalk(String email, Pageable pageable) {
        ItCookUser findByUserEmail = userDomainService.findUserByEmail(email);

        Page<Object[]> cookTalkFeedDtos = postDomainService.fetchFindFollowingCookTalk(findByUserEmail.getFollow(), pageable);

        return getCookTalkResponse(cookTalkFeedDtos, findByUserEmail);
    }

    private CookTalkResponse getCookTalkResponse(Page<Object[]> postData, ItCookUser findByUserEmail) {
        List<CookTalkDto> cookTalkDtos = new ArrayList<>();

        for (Object[] postDatum : postData) {
            Post post = (Post) postDatum[0];
            ItCookUser itCookUser = (ItCookUser) postDatum[1];
            cookTalkDtos.add(new CookTalkDto(post, itCookUser));
        }

        //팔로우 여부 검증
        followValidation(findByUserEmail, cookTalkDtos);

        //좋아요 여부 검증
        likedValidation(findByUserEmail, cookTalkDtos);

        return CookTalkResponse.of(cookTalkDtos, postData.hasNext(), postData.getTotalElements(), postData.getTotalPages());
    }

    private void followValidation(ItCookUser findByUserEmail, List<CookTalkDto> cookTalkDtos) {

        cookTalkDtos.forEach(cookTalkDto -> {
            boolean followingCheck = postValidationUseCase.getFollowingCheck(cookTalkDto.getUserId(), findByUserEmail.getFollow());
            cookTalkDto.followCheckSet(followingCheck);
        });
    }

    private void likedValidation(ItCookUser findByUserEmail, List<CookTalkDto> cookTalkDtos) {
        // postId 중복제거
        Set<Long> postIdData = cookTalkDtos.stream()
                .map(CookTalkDto::getPostId)
                .collect(Collectors.toSet());

        // postId에 해당하는 좋아요 조회
        Map<Long, List<Liked>> postIdToLikedMap = likedDomainService.getFindAllLiked(new ArrayList<>(postIdData))
                .stream()
                .collect(Collectors.groupingBy(Liked::getPostId));

        // 각 게시글에 대한 좋아요 유효성 검사 후 cookTalkDto의 상태를 설정
        for (CookTalkDto cookTalkDto : cookTalkDtos) {
            Long postId = cookTalkDto.getPostId();
            List<Liked> filteredLiked = postIdToLikedMap.getOrDefault(postId, Collections.emptyList());
            cookTalkDto.likedInfoSet(filteredLiked.size(), postValidationUseCase.getLikedValidation(filteredLiked, findByUserEmail.getId()));
        }
    }


}
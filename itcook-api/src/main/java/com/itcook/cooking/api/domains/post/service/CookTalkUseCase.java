package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.cooktalk.CookTalkDto;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkFeedsResponse;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import com.itcook.cooking.domain.domains.like.service.LikedService;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
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

    private final PostService postService;
    private final UserService userService;
    private final LikedService likedService;
    private final PostValidationUseCase postValidationUseCase;

    /**
     * CookTalk 피드 조회
     */
    public PageResponse<CookTalkFeedsResponse> getCookTalkFeeds(String email, Pageable pageable) {
        ItCookUser findByUserEmail = userService.findUserByEmail(email);

        Page<CookTalkFeedDto> cookTalkFeeds = postService
                .getCookTalkFeeds(findByUserEmail.getId(), pageable);

        Page<CookTalkFeedsResponse> cookTalkResponse = CookTalkFeedsResponse
                .fromCookTalkFeedDto(cookTalkFeeds, findByUserEmail);

        return PageResponse.of(cookTalkResponse);
    }

    public void getFollowingTalk(String email, Pageable pageable) {
//        ItCookUser findByUserEmail = userService.findUserByEmail(email);
//        Page<Object[]> cookTalkFeedDtos = postService.fetchFindFollowingCookTalk(findByUserEmail.getFollow(), pageable);

//        return getCookTalkResponse(cookTalkFeedDtos, findByUserEmail);
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
        Map<Long, List<Liked>> postIdToLikedMap = likedService.getFindAllLiked(new ArrayList<>(postIdData))
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
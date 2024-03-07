package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CookTalkUseCase {

    private final PostDomainService postDomainService;
    private final UserDomainService userDomainService;

    public List<CookTalkResponse> getCookTalk(String email) {
        // 이메일로 사용자 정보와 팔로우 목록을 한 번에 가져옵니다.
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(email);

        // 본인을 제외한 사용자의 게시글 조회
        List<Post> postAllData = postDomainService.fetchFindAllByUserIdNotWithUsers(findByUserEmail.getId());

        return getPostAndResponses(findByUserEmail, postAllData);
    }

    public List<CookTalkResponse> getFollowingTalk(String email) {
        // 유저 정보 검색
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(email);

        // 게시글 정보 검색
        List<Post> postFollowingData = postDomainService.fetchFindFollowingCookTalk(findByUserEmail.getFollow());

        return getPostAndResponses(findByUserEmail, postFollowingData);
    }


    private List<CookTalkResponse> getPostAndResponses(ItCookUser findByUserEmail, List<Post> postData) {
        //팔로우 여부 Set
        Set<Long> followingSet = new HashSet<>(findByUserEmail.getFollow());

        Map<Long, CookTalkUserMapping> userMap = userDomainService.fetchFindUserByIdIn(postData.stream()
                        .map(Post::getUserId)
                        .toList())
                .stream()
                .collect(Collectors.toMap(CookTalkUserMapping::getId, Function.identity()));

        // 최종 결과를 저장할 리스트를 생성합니다.
        return postData.parallelStream()
                .map(post -> {
                    // 팔로우 여부 확인
                    boolean isFollowing = followingSet.contains(post.getUserId());
                    // post 작성자의 데이터
                    CookTalkUserMapping user = userMap.get(post.getUserId());
                    return CookTalkResponse.of(post, user, isFollowing);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

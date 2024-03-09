package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.CookTalkDto;
import com.itcook.cooking.api.domains.post.dto.RecipeDto;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.user.entity.Archive;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostValidationUseCase {

    private final UserDomainService userDomainService;

    //유저 매핑
    public <T> List<T> postUserMatchingValidation(List<Post> postData, BiFunction<Post, CookTalkUserMapping, T> dtoConstructor) {

        //해당 포스트와 유저 정보를 매핑
        Map<Long, CookTalkUserMapping> userMap = userDomainService.fetchFindUserByIdIn(postData.stream()
                        .map(Post::getUserId)
                        .toList())
                .stream()
                .collect(Collectors.toMap(CookTalkUserMapping::getId, Function.identity()));

        return postData.parallelStream()
                .map(post -> {
                    CookTalkUserMapping user = userMap.get(post.getUserId());
                    return dtoConstructor.apply(post, user);
                })
                .collect(Collectors.toList());
    }

    //팔로우 여부
    public <T> void getFollowingCheck(T dto, Set<Long> followingSet) {

        if (dto instanceof CookTalkDto) {
            CookTalkDto cookTalkDto = (CookTalkDto) dto;
            boolean isFollowChk = followingSet.contains(cookTalkDto.getUserId());
            cookTalkDto.setFollowChk(isFollowChk);
        } else if (dto instanceof RecipeDto) {
            RecipeDto recipeDto = (RecipeDto) dto;
            boolean isFollowChk = followingSet.contains(recipeDto.getUserId());
            recipeDto.setFollowCheck(isFollowChk);
        } else {
            throw new IllegalArgumentException("잘 못된 DTO : " + dto.getClass().getName());
        }

    }

    //좋아요 여부
    public boolean getLikedValidation(List<Liked> findAllLikedData, Long userId, Long postId) {
        return findAllLikedData.stream()
                .anyMatch(liked -> liked.getPostId().equals(postId) && liked.getItCookUserId().equals(userId));
    }


    //보관함 여부
    public boolean getArchiveValidation(List<Archive> itCookUserLikedData, Long postId) {
        return itCookUserLikedData.stream()
                .anyMatch(archive -> archive.getPostId() == postId);
    }


}

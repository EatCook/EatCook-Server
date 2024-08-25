package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.archive.service.ArchiveService;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ArchiveUseCase {

    private final UserService userService;
    private final PostService postService;
    private final ArchiveService archiveService;

    public void addArchive(String email, Long postId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(postId);

        archiveService.saveArchive(findByItCookUser.getId(), findByPost.getId());
    }

    public void removeArchive(String email, Long postId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(postId);

        archiveService.removeArchive(findByItCookUser.getId(), findByPost.getId());
    }

}

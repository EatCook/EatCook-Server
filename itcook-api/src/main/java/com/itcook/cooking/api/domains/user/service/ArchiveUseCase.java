package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.archive.entity.Archive;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.archive.service.ArchiveService;
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

    public void archiveAdd(String email, Long postId) {

        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(postId);

        archiveService.validateDuplicateArchive(findByItCookUser.getId(), findByPost.getId());

        Archive newArchive = Archive.builder()
                .itCookUserId(findByItCookUser.getId())
                .postId(findByPost.getId())
                .build();
        archiveService.saveArchive(newArchive);
    }

    public void archiveDel(String email, Long postId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(postId);

        Archive findArchive = archiveService.validateEmptyArchive(findByItCookUser.getId(), findByPost.getId());

        archiveService.removeArchive(findArchive);
    }

}

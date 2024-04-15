package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.ArchiveErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.Archive;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.ArchiveDomainService;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ArchiveUseCase {

    private final UserDomainService userDomainService;
    private final PostDomainService postDomainService;
    private final ArchiveDomainService archiveDomainService;

    public void archiveAdd(String email, Long postId) {

        ItCookUser findByItCookUser = userDomainService.fetchFindByEmail(email);
        Post findByPost = postDomainService.fetchFindByPost(postId).get();

        List<Archive> findByArchive = archiveDomainService.getFindByItCookUserId(findByItCookUser.getId());


        boolean archiveValid = false;
        for (Archive archive : findByArchive) {
            archiveValid = Objects.equals(archive.getPostId(), findByPost.getId());
        }

        if (archiveValid) {
            throw new ApiException(ArchiveErrorCode.ALREADY_ADD_ARCHIVE);
        }

        Archive newArchive = Archive.builder()
                .itCookUserId(findByItCookUser.getId())
                .postId(findByPost.getId())
                .build();
        archiveDomainService.saveArchive(newArchive);

    }

    public void archiveDel(String email, Long postId) {
        ItCookUser findByItCookUser = userDomainService.fetchFindByEmail(email);
        Post findByPost = postDomainService.fetchFindByPost(postId).get();

        List<Archive> findByArchive = archiveDomainService.getFindByItCookUserId(findByItCookUser.getId());

        boolean archiveValid = false;
        Long archiveId = null;
        for (Archive archive : findByArchive) {
            archiveId = archive.getId();
            archiveValid = Objects.equals(archive.getPostId(), findByPost.getId());
        }

        if (!archiveValid) {
            throw new ApiException(ArchiveErrorCode.NOT_SAVED_IN_ARCHIVE);
        }

        archiveDomainService.removeArchive(archiveId);
    }

}

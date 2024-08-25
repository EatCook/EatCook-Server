package com.itcook.cooking.domain.domains.archive.service;

import com.itcook.cooking.domain.domains.archive.domain.adaptor.ArchiveAdaptor;
import com.itcook.cooking.domain.domains.archive.domain.dto.ArchivePost;
import com.itcook.cooking.domain.domains.archive.domain.entity.Archive;
import com.itcook.cooking.domain.domains.archive.domain.repository.ArchiveQuerydslRepository;

import java.util.List;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {

    private final ArchiveAdaptor archiveAdaptor;
    private final ArchiveQuerydslRepository archiveQuerydslRepository;

    @Transactional
    public void saveArchive(Long userId, Long postId) {
        validateDuplicateArchive(userId, postId);

        Archive newArchive = Archive.addArchive(userId, postId);
        archiveAdaptor.saveArchive(newArchive);
    }

    public Archive validateEmptyArchive(Long userId, Long postId) {
        return archiveAdaptor.checkEmptyArchive(userId, postId);
    }

    @Transactional
    public void removeArchive(Archive archive) {
        archiveAdaptor.removeArchiveEntity(archive);
    }

    public List<ArchivePost> getArchivesPosts(Long userId) {
        return archiveQuerydslRepository.findPostsByUserId(userId);
    }

    private void validateDuplicateArchive(Long userId, Long postId) {
        archiveAdaptor.checkDuplicateArchive(userId, postId);
    }

}

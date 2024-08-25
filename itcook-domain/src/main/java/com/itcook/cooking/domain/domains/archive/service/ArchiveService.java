package com.itcook.cooking.domain.domains.archive.service;

import com.itcook.cooking.domain.domains.archive.domain.adaptor.ArchiveAdaptor;
import com.itcook.cooking.domain.domains.archive.domain.dto.ArchivePost;
import com.itcook.cooking.domain.domains.archive.domain.entity.Archive;
import com.itcook.cooking.domain.domains.archive.domain.entity.ArchiveValidator;
import com.itcook.cooking.domain.domains.archive.domain.repository.ArchiveQuerydslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {

    private final ArchiveAdaptor archiveAdaptor;
    private final ArchiveQuerydslRepository archiveQuerydslRepository;
    private final ArchiveValidator archiveValidator;

    @Transactional
    public void saveArchive(Long userId, Long postId) {
        archiveAdaptor.checkDuplicateArchive(userId, postId);

        Archive newArchive = Archive.addArchive(userId, postId, archiveValidator);
        archiveAdaptor.saveArchive(newArchive);
    }

    @Transactional
    public void removeArchive(Long userId, Long postId) {
        Archive findArchive = archiveAdaptor.checkEmptyArchive(userId, postId);

        archiveAdaptor.removeArchiveEntity(findArchive);
    }

    public List<ArchivePost> getArchivesPosts(Long userId) {
        return archiveQuerydslRepository.findPostsByUserId(userId);
    }

}

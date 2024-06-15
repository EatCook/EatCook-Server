package com.itcook.cooking.domain.domains.archive.service;

import com.itcook.cooking.domain.domains.archive.adaptor.ArchiveAdaptor;
import com.itcook.cooking.domain.domains.archive.dto.ArchivePost;
import com.itcook.cooking.domain.domains.archive.entity.Archive;
import com.itcook.cooking.domain.domains.archive.repository.ArchiveQuerydslRepository;
import com.itcook.cooking.domain.domains.archive.repository.ArchiveRepository;
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
    private final ArchiveRepository archiveRepository;
    private final ArchiveQuerydslRepository archiveQuerydslRepository;

    public void validateDuplicateArchive(Long userId, Long postId) {
         archiveAdaptor.checkDuplicateArchive(userId, postId);
    }
    public Archive validateEmptyArchive(Long userId, Long postId) {
         return archiveAdaptor.checkEmptyArchive(userId, postId);
    }

    @Transactional
    public void saveArchive(Archive archive) {
        archiveAdaptor.saveArchive(archive);
    }

    @Transactional
    public void removeArchive(Archive archive) {
        archiveAdaptor.removeArchiveEntity(archive);
    }

    public List<ArchivePost> getArchivesPosts(Long userId) {
        return archiveQuerydslRepository.findPostsByUserId(userId);
    }
}

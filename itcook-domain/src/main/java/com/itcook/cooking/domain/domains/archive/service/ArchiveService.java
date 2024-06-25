package com.itcook.cooking.domain.domains.archive.service;

import com.itcook.cooking.domain.domains.archive.domain.adaptor.ArchiveAdaptor;
import com.itcook.cooking.domain.domains.archive.domain.dto.ArchivePost;
import com.itcook.cooking.domain.domains.archive.domain.entity.Archive;
import com.itcook.cooking.domain.domains.archive.domain.repository.ArchiveQuerydslRepository;
import java.util.List;
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

package com.itcook.cooking.domain.domains.archive.service;

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
public class ArchiveDomainService {

    private final ArchiveRepository archiveRepository;
    private final ArchiveQuerydslRepository archiveQuerydslRepository;

    public List<Archive> getFindByUserId(Long userId) {
        return archiveRepository.findByItCookUserId(userId);
    }

    @Transactional
    public void saveArchive(Archive newArchive) {
        archiveRepository.save(newArchive);
    }

    @Transactional
    public void removeArchive(Long archiveId) {
        archiveRepository.deleteById(archiveId);
    }

    public List<ArchivePost> getArchivesPosts(Long userId) {
        return archiveQuerydslRepository.findPostsByUserId(userId);
    }
}

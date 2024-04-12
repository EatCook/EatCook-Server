package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.errorcode.ArchiveErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.Archive;
import com.itcook.cooking.domain.domains.user.repository.ArchiveRepository;
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

    public List<Archive> getFindByItCookUserId(Long userId) {
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
}

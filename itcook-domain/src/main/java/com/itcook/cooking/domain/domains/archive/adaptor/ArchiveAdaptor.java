package com.itcook.cooking.domain.domains.archive.adaptor;

import com.itcook.cooking.domain.common.errorcode.ArchiveErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.archive.entity.Archive;
import com.itcook.cooking.domain.domains.archive.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArchiveAdaptor {

    private final ArchiveRepository archiveRepository;

    public void checkDuplicateArchive(Long userId, Long postId) {
        archiveRepository.findByItCookUserIdAndPostId(userId, postId)
                .ifPresent(it -> {
                    throw new ApiException(ArchiveErrorCode.ALREADY_ADD_ARCHIVE);
                });
    }

    public Archive checkEmptyArchive(Long userId, Long postId) {
        return archiveRepository.findByItCookUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new ApiException(ArchiveErrorCode.NOT_SAVED_IN_ARCHIVE));
    }

    public void saveArchive(Archive archive) {
        archiveRepository.save(archive);
    }

    public void removeArchiveEntity(Archive archive) {
        archiveRepository.delete(archive);
    }
}

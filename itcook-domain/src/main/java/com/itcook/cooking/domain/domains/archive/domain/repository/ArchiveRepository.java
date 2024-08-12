package com.itcook.cooking.domain.domains.archive.domain.repository;

import com.itcook.cooking.domain.domains.archive.domain.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByItCookUserIdAndPostId(Long itCookUserId, Long postId);
}

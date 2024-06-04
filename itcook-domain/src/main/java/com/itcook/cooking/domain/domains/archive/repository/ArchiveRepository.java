package com.itcook.cooking.domain.domains.archive.repository;

import com.itcook.cooking.domain.domains.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByItCookUserIdAndPostId(Long itCookUserId, Long postId);
}

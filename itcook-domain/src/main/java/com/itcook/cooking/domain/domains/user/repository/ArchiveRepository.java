package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.domain.domains.user.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    List<Archive> findByItCookUserId(Long itCookUserId);
}

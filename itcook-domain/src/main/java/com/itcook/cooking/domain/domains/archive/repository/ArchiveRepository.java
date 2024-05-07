package com.itcook.cooking.domain.domains.archive.repository;

import com.itcook.cooking.domain.domains.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    List<Archive> findByItCookUserId(Long itCookUserId);
}

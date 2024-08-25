package com.itcook.cooking.domain.domains.archive.domain.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Archive extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id")
    private Long id;

    @Column(name = "user_id")
    private Long itCookUserId;

    @Column(name = "post_id")
    private Long postId;

    @Builder
    private Archive(Long itCookUserId, Long postId) {
        this.itCookUserId = itCookUserId;
        this.postId = postId;
    }

    public static Archive addArchive(Long userId, Long postId, ArchiveValidator archiveValidator) {
        Archive archive = Archive.builder()
                .itCookUserId(userId)
                .postId(postId)
                .build();
        archiveValidator.validateAdd(archive);
        return archive;
    }

}

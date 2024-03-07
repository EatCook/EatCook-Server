package com.itcook.cooking.domain.domains.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id")
    private Long id;

    @Column(name = "user_id")
    private Long itCookUserId;

    @Column(name = "post_id")
    private Long postId;

    @Builder
    public Archive(Long itCookUserId, Long postId) {
        this.itCookUserId = itCookUserId;
        this.postId = postId;
    }

}

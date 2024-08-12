package com.itcook.cooking.domain.domains.archive.domain.repository;


import static com.itcook.cooking.domain.domains.archive.domain.entity.QArchive.archive;
import static com.itcook.cooking.domain.domains.post.domain.entity.QPost.post;

import com.itcook.cooking.domain.domains.archive.domain.dto.ArchivePost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArchiveQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ArchivePost> findPostsByUserId(Long userId) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    ArchivePost.class,
                    post.id,
                    post.postImagePath
                    )
            )
            .from(archive)
            .join(post).on(post.id.eq(archive.postId))
            .where(archive.itCookUserId.eq(userId))
            .orderBy(archive.id.desc())
            .fetch();
    }

}

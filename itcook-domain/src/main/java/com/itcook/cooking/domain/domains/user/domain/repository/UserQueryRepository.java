package com.itcook.cooking.domain.domains.user.domain.repository;

import static com.itcook.cooking.domain.domains.post.domain.entity.QPost.post;
import static com.itcook.cooking.domain.domains.user.domain.entity.QItCookUser.itCookUser;

import com.itcook.cooking.domain.domains.user.domain.repository.dto.UserPostCount;
import com.itcook.cooking.domain.domains.user.domain.enums.UserBadge;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public long getFollowerCounts(Long userId) {
        JPAQuery<Long> countQuery = jpaQueryFactory.select(itCookUser.count())
            .from(itCookUser)
            .where(itCookUser.follow.contains(userId));
        Long count = countQuery.fetchOne();
        return count == null ? 0 : count;
    }
    
    public List<UserPostCount> getUserPostCount(List<Long> userIds) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    UserPostCount.class,
                    itCookUser.id,
                    post.count()
                )
            )
            .from(itCookUser)
            .leftJoin(post).on(itCookUser.id.eq(post.userId))
            .where(itCookUser.id.in(userIds))
            .groupBy(itCookUser.id)
            .fetch();
    }

    @Transactional
    public void updateBadge(UserBadge userBadge, List<Long> userIds) {
        log.info("유저 뱃지 : {}, 유저 아이디 : {}", userBadge, userIds);
        jpaQueryFactory
            .update(itCookUser)
            .set(itCookUser.badge, userBadge)
            .where(itCookUser.id.in(userIds))
            .execute()
            ;
    }

}

package com.itcook.cooking.domain.domains.user.repository;

import static com.itcook.cooking.domain.domains.post.entity.QPost.post;
import static com.itcook.cooking.domain.domains.user.entity.QItCookUser.itCookUser;
import static com.querydsl.jpa.JPAExpressions.select;

import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserState;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Transactional
    public void updateUserBadge() {
        List<Long> userIds = jpaQueryFactory
            .select(post.userId)
            .from(post)
            .groupBy(post.userId)
            .having(post.count().goe(50))
            .fetch();

        if (CollectionUtils.isEmpty(userIds)) return;

        jpaQueryFactory
            .update(itCookUser)
            .where(itCookUser.id.in(
                userIds
            ))
            .set(itCookUser.badge, UserBadge.GIBBAB_GOSU)
            .execute();

    }
}

package com.itcook.cooking.domain.domains.user.repository;

import static com.itcook.cooking.domain.domains.user.entity.QItCookUser.itCookUser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public long getFollowerCounts(Long userId) {
        return jpaQueryFactory.selectFrom(itCookUser)
            .where(itCookUser.follow.contains(userId))
            .fetchCount();
    }

}

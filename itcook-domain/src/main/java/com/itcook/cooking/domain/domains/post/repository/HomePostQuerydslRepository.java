package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.dto.HomePostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itcook.cooking.domain.domains.archive.entity.QArchive.archive;
import static com.itcook.cooking.domain.domains.like.entity.QLiked.liked;
import static com.itcook.cooking.domain.domains.post.entity.QPost.post;
import static com.itcook.cooking.domain.domains.post.entity.QPostCookingTheme.postCookingTheme;
import static com.itcook.cooking.domain.domains.user.entity.QItCookUser.itCookUser;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomePostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<HomePostDto> findPostsWithLikedAndArchiveDtoByCookingTheme(CookingType cookingTheme, Long userId, Pageable pageable) {
        Set<Long> likedPostIds = new HashSet<>(jpaQueryFactory.select(liked.postId)
                .from(liked)
                .where(liked.itCookUserId.eq(userId))
                .fetch());

        Set<Long> archivePostIds = new HashSet<>(jpaQueryFactory.select(archive.postId)
                .from(archive)
                .where(archive.itCookUserId.eq(userId))
                .fetch());

        List<HomePostDto> posts = jpaQueryFactory.select(
                        Projections.constructor(
                                HomePostDto.class,
                                post.id,
                                post.postImagePath,
                                post.recipeName,
                                post.recipeTime,
                                itCookUser.profile,
                                itCookUser.nickName,
                                liked.count()
                        )
                )
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .join(postCookingTheme).on(post.id.eq(postCookingTheme.post.id))
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        postCookingTheme.cookingType.eq(cookingTheme),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                )
                .groupBy(post.id, itCookUser.profile, itCookUser.nickName)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (HomePostDto postDto : posts) {
            postDto.setLikedCheck(likedPostIds.contains(postDto.getPostId()));
            postDto.setArchiveCheck(archivePostIds.contains(postDto.getPostId()));
        }

        JPAQuery<Long> countQuery = jpaQueryFactory.select(post.count())
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .join(postCookingTheme).on(post.id.eq(postCookingTheme.post.id))
                .where(post.postFlag.eq(PostFlag.ACTIVATE));

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }


}

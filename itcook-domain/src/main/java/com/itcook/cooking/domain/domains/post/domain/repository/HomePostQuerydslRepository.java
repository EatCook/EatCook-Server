package com.itcook.cooking.domain.domains.post.domain.repository;

import static com.itcook.cooking.domain.domains.archive.domain.entity.QArchive.archive;
import static com.itcook.cooking.domain.domains.like.domain.entity.QLiked.liked;
import static com.itcook.cooking.domain.domains.post.domain.entity.QPost.post;
import static com.itcook.cooking.domain.domains.post.domain.entity.QPostCookingTheme.postCookingTheme;
import static com.itcook.cooking.domain.domains.user.domain.entity.QItCookUser.itCookUser;

import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeInterestDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeSpecialDto;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
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


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomePostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<HomeInterestDto> findPostsWithLikedAndArchiveDtoByCookingTheme(CookingType cookingTheme, Long userId, Pageable pageable) {
        Set<Long> likedPostIds = findLikedByUserId(userId);

        Set<Long> archivePostIds = findArchiveByUserId(userId);

        List<HomeInterestDto> posts = jpaQueryFactory.select(
                        Projections.constructor(
                                HomeInterestDto.class,
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
                .groupBy(post.id)
                .orderBy(post.lastModifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (HomeInterestDto postDto : posts) {
            postDto.setLikedCheck(likedPostIds.contains(postDto.getPostId()));
            postDto.setArchiveCheck(archivePostIds.contains(postDto.getPostId()));
        }

        JPAQuery<Long> countQuery = jpaQueryFactory.select(post.count())
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .join(postCookingTheme).on(post.id.eq(postCookingTheme.post.id))
                .where(
                        postCookingTheme.cookingType.eq(cookingTheme),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    public Page<HomeSpecialDto> findPostsWithLikedAndArchiveDtoByLifeTypeDefaultHealthDiet(LifeType lifeType, Long userId, Pageable pageable) {
        Set<Long> likedPostIds = findLikedByUserId(userId);

        Set<Long> archivePostIds = findArchiveByUserId(userId);

        List<HomeSpecialDto> posts = jpaQueryFactory.select(
                        Projections.constructor(
                                HomeSpecialDto.class,
                                post.id,
                                post.postImagePath,
                                post.recipeName,
                                post.introduction,
                                post.recipeTime,
                                liked.count()
                        )
                )
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .join(postCookingTheme).on(post.id.eq(postCookingTheme.post.id))
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        post.lifeTypes.contains(lifeType),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                )
                .groupBy(post.id, itCookUser.profile, itCookUser.nickName)
                .orderBy(post.lastModifiedAt.desc(), liked.postId.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (HomeSpecialDto postDto : posts) {
            postDto.setLikedCheck(likedPostIds.contains(postDto.getPostId()));
            postDto.setArchiveCheck(archivePostIds.contains(postDto.getPostId()));
        }

        JPAQuery<Long> countQuery = jpaQueryFactory.select(post.count())
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .join(postCookingTheme).on(post.id.eq(postCookingTheme.post.id))
                .where(
                        post.lifeTypes.contains(lifeType),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }


    private Set<Long> findLikedByUserId(Long userId) {
        return new HashSet<>(jpaQueryFactory.select(liked.postId)
                .from(liked)
                .where(liked.itCookUserId.eq(userId))
                .fetch());
    }

    private Set<Long> findArchiveByUserId(Long userId) {
        return new HashSet<>(jpaQueryFactory.select(archive.postId)
                .from(archive)
                .where(archive.itCookUserId.eq(userId))
                .fetch());
    }

}

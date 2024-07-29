package com.itcook.cooking.domain.domains.post.domain.repository;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFollowDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.SearchPostDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPagePostInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itcook.cooking.domain.domains.archive.domain.entity.QArchive.archive;
import static com.itcook.cooking.domain.domains.like.domain.entity.QLiked.liked;
import static com.itcook.cooking.domain.domains.post.domain.entity.QPost.post;
import static com.itcook.cooking.domain.domains.post.domain.entity.QPostCookingTheme.postCookingTheme;
import static com.itcook.cooking.domain.domains.user.domain.entity.QItCookUser.itCookUser;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<MyRecipeResponse> findPostsWithLiked(Long userId, Pageable pageable) {
        List<MyRecipeResponse> posts = jpaQueryFactory.select(
                        Projections.constructor(
                                MyRecipeResponse.class,
                                post.id,
                                post.postImagePath,
                                post.recipeName,
                                post.introduction,
                                liked.postId.count()
                        )
                )
                .from(post)
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        post.userId.eq(userId),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                )
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(
                        post.count()
                )
                .from(post)
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        post.userId.eq(userId),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    /**
     * 작성일 기준 레시피 정보 조회
     */
    public Page<CookTalkFeedDto> findCookTalkPostsWithLiked(Long userId, Pageable pageable) {
        Set<Long> likedByUserId = findLikedByUserId(userId);
        List<CookTalkFeedDto> posts = jpaQueryFactory.select(
                        Projections.constructor(
                                CookTalkFeedDto.class,
                                itCookUser.id,
                                itCookUser.email,
                                post.id,
                                post.postImagePath,
                                post.recipeName,
                                post.introduction,
                                post.lastModifiedAt,
                                liked.postId.count()
                        )
                )
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        post.postFlag.eq(PostFlag.ACTIVATE)
                )
                .groupBy(post.id)
                .orderBy(post.lastModifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (CookTalkFeedDto postDto : posts) {
            postDto.setLikedCheck(likedByUserId.contains(postDto.getPostId()));
        }

        JPAQuery<Long> countQuery = jpaQueryFactory.select(
                        post.count()
                )
                .from(post)
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        post.userId.eq(userId),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    public List<Post> findNamesWithPagination(Long lastId
            , List<String> names, Integer size) {

        return jpaQueryFactory.selectFrom(post)
                .distinct()
                .where(
                        lessThanId(lastId)
                        , post.postFlag.eq(PostFlag.ACTIVATE)
                        , combineExpr(containsRecipeNames(names), containsIngredientNames(names))
                )
                .orderBy(post.createdAt.desc())
                .limit(size)
                .fetch();
    }

    public List<SearchPostDto> findAllWithPagination
            (
                    Long lastId, List<String> recipeNames, List<String> ingredientNames,
                    Integer size
            ) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                SearchPostDto.class, post.id,
                                post.recipeName,
                                post.introduction,
                                post.postImagePath,
                                liked.postId.count(),
                                itCookUser.nickName
                        ))
                .from(post)
                .innerJoin(itCookUser).on(post.userId.eq(itCookUser.id))
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        lessThanId(lastId),
                        post.postFlag.eq(PostFlag.ACTIVATE),
                        containsRecipeNames(recipeNames),
                        containsIngredientNames(ingredientNames)
                )
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .limit(size)
                .fetch();
    }

    public List<SearchPostDto> findAllWithPagination2
            (
                    Long lastId, String recipeName, String ingredientName, Integer size
            ) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                SearchPostDto.class, post.id,
                                post.recipeName,
                                post.introduction,
                                post.postImagePath,
                                liked.postId.count(),
                                itCookUser.nickName
                        ))
                .from(post)
                .innerJoin(itCookUser).on(post.userId.eq(itCookUser.id))
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        lessThanId(lastId),
                        post.postFlag.eq(PostFlag.ACTIVATE),
                        containsRecipeName(recipeName),
                        containsIngredientName(ingredientName)
                )
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .limit(size)
                .fetch();
    }


    private BooleanExpression lessThanId(Long lastId) {
        return lastId != null ? post.id.lt(lastId) : null;
    }

    private BooleanExpression combineExpr(BooleanExpression containsRecipeName,
                                          BooleanExpression containsIngredientNames) {
        // 2개의 Expr이 null 일 경우에만 null을 리턴
        if (containsRecipeName == null && containsIngredientNames == null) {
            return null;
        }
        // 2개의 Expr이 null이 아닐 경우에는 or 조건으로 합쳐서 리턴
        return containsRecipeName.or(containsIngredientNames);
    }

    private BooleanExpression containsRecipeName(String recipeName) {
        return recipeName != null ? post.recipeName.containsIgnoreCase(recipeName) : null;
    }

    private BooleanExpression containsIngredientName(String ingredientName) {
        return ingredientName != null ? post.foodIngredients.any().contains(ingredientName) : null;
    }

    private BooleanExpression containsRecipeNames(List<String> recipeNames) {
        if (CollectionUtils.isEmpty(recipeNames)) {
            return null;
        }
        BooleanExpression expr = null;
        for (String name : recipeNames) {
            BooleanExpression currentExpr = post.recipeName.containsIgnoreCase(name);
            if (expr == null) {
                expr = currentExpr;
            } else {
                expr = expr.or(currentExpr);
            }
        }
        return expr;
    }

    private BooleanExpression containsIngredientNames(List<String> ingredientNames) {
        if (CollectionUtils.isEmpty(ingredientNames)) {
            return null;
        }

        BooleanExpression expr = null;
        for (String ingredientName : ingredientNames) {
            BooleanExpression currentExpr = post.foodIngredients.any().contains(ingredientName);
            if (expr == null) {
                expr = currentExpr;
            } else {
                expr = expr.or(currentExpr);
            }
        }
        return expr;
//        }
    }

    public Page<OtherPagePostInfoResponse> getOtherPagePostInfo(
            Long userId, Long otherUserId, Pageable pageable
    ) {
        Set<Long> likedPostIds = findLikedByUserId(userId);

        Set<Long> archivePostIds = findArchiveByUserId(userId);

        List<OtherPagePostInfoResponse> posts = jpaQueryFactory.select(
                        Projections.constructor(
                                OtherPagePostInfoResponse.class,
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
                        itCookUser.id.eq(otherUserId),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                )
                .groupBy(post.id)
                .orderBy(post.lastModifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (OtherPagePostInfoResponse postDto : posts) {
            postDto.setLikedCheck(likedPostIds.contains(postDto.getPostId()));
            postDto.setArchiveCheck(archivePostIds.contains(postDto.getPostId()));
        }

        JPAQuery<Long> countQuery = jpaQueryFactory.select(post.count())
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .join(postCookingTheme).on(post.id.eq(postCookingTheme.post.id))
                .where(
                        itCookUser.id.eq(userId),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    /**
     * follow한 게시글 조회
     */
    public Page<CookTalkFollowDto> findFollowPostWithLiked(Long authUserId, List<Long> userId, Pageable pageable) {
        Set<Long> likedByUserId = findLikedByUserId(authUserId);
        List<CookTalkFollowDto> posts = jpaQueryFactory.select(
                        Projections.constructor(
                                CookTalkFollowDto.class,
                                itCookUser.id,
                                itCookUser.email,
                                post.id,
                                post.postImagePath,
                                post.recipeName,
                                post.introduction,
                                post.lastModifiedAt,
                                liked.postId.count()
                        )
                )
                .from(post)
                .join(itCookUser).on(itCookUser.id.eq(post.userId))
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        itCookUser.id.in(userId),
                        post.postFlag.eq(PostFlag.ACTIVATE)
                )
                .groupBy(post.id)
                .orderBy(post.lastModifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (CookTalkFollowDto postDto : posts) {
            postDto.setLikedCheck(likedByUserId.contains(postDto.getPostId()));
        }

        JPAQuery<Long> countQuery = jpaQueryFactory.select(
                        post.count()
                )
                .from(post)
                .leftJoin(liked).on(post.id.eq(liked.postId))
                .where(
                        itCookUser.id.in(userId),
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


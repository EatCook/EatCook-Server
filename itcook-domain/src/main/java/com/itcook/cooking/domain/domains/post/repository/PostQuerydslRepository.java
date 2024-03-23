package com.itcook.cooking.domain.domains.post.repository;

import static com.itcook.cooking.domain.domains.post.entity.QLiked.liked;
import static com.itcook.cooking.domain.domains.post.entity.QPost.post;
import static com.itcook.cooking.domain.domains.user.entity.QItCookUser.itCookUser;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.dto.SearchPostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

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
        Long lastId, List<String> recipeNames ,List<String> ingredientNames, Integer size
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
            Long lastId, String recipeName ,String ingredientName, Integer size
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

    private BooleanExpression combineExpr(BooleanExpression containsRecipeName, BooleanExpression containsIngredientNames) {
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

}

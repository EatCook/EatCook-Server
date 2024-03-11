package com.itcook.cooking.domain.domains.post.repository;

import static com.itcook.cooking.domain.domains.post.entity.QPost.post;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
public class PostQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // TODO
    public List<Post> findAllWithPagination(Long lastId
        , List<String> names, Integer size) {

        return jpaQueryFactory.selectFrom(post)
            .distinct()
            .where(
                lessThanId(lastId)
                , post.postFlag.eq(PostFlag.ACTIVATE)
                , combineExpr(containsRecipeName(names), containsIngredientNames(names))
            )
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

    private BooleanExpression containsRecipeName(List<String> recipeNames) {
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

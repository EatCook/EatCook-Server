package com.itcook.cooking.domain.domains.post.repository;

import static com.itcook.cooking.domain.domains.post.entity.QPost.post;

import com.itcook.cooking.domain.domains.post.entity.Post;
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
        , List<String> names, Long size) {

        return jpaQueryFactory.selectFrom(post)
            .distinct()
            .where(lessThanId(lastId)
                , containsRecipeName(names).or(containsIngredientNames(names)))
            .orderBy(post.createdAt.desc())
            .limit(size)
            .fetch();
    }

    private BooleanExpression lessThanId(Long lastId) {
        return lastId != null ? post.id.lt(lastId) : null;
    }

    private BooleanExpression containsRecipeName(List<String> recipeNames) {
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

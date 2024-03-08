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
        , String recipeName, List<String> ingredientNames, Long size) {

        return jpaQueryFactory.selectFrom(post)
            .where(lessThanId(lastId)
                , containsRecipeName(recipeName)
                , containsIngredientNames(ingredientNames))
            .orderBy(post.createdAt.desc())
            .limit(size)
            .fetch();
    }

    private BooleanExpression lessThanId(Long lastId) {
        return lastId != null ? post.id.lt(lastId) : null;
    }

    private BooleanExpression containsRecipeName(String recipeName) {
        return recipeName != null ? post.recipeName.contains(recipeName) : null;
    }

    private BooleanExpression containsIngredientNames(List<String> ingredientNames) {
        if (CollectionUtils.isEmpty(ingredientNames)) {
            return null;
        } else {
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
        }
    }

}

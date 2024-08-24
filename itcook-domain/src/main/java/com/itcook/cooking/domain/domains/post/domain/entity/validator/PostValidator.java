package com.itcook.cooking.domain.domains.post.domain.entity.validator;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.PostCookingTheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_MAX_TIME;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_MIN_TIME;

@Component
@RequiredArgsConstructor
public class PostValidator {
    /**
     * 게시글 생성 검증
     */
    public void validateAdd(Post post) {
        int recipeTime = post.getRecipeTime();

        Assert.hasText(post.getRecipeName(), "게시글 제목이 누락되었습니다.");
        Assert.isTrue(RECIPE_MIN_TIME <= recipeTime && recipeTime <= RECIPE_MAX_TIME, "게시글 조리시간 범위를 벗어났습니다.");
        Assert.hasText(post.getIntroduction(), "게시글 설명이 누락되었습니다.");
        Assert.notNull(post.getUserId(), "작성자가 누락되었습니다.");
        Assert.notNull(post.getFoodIngredients(), "재료가 누락되었습니다.");
        Assert.notNull(post.getLifeTypes(), "요리 유형이 누락되었습니다.");
        Assert.notNull(post.getPostFlag(), "게시글 상태가 누락되었습니다.");
    }

    public void validatePostCookingTheme(List<PostCookingTheme> postCookingThemes) {
        Assert.notEmpty(postCookingThemes, "요리 테마가 누락되었습니다.");

        for (PostCookingTheme pct : postCookingThemes) {
            Assert.notNull(pct.getCookingType(), "요리 테마가 누락되었습니다.");
            Assert.notNull(pct.getPost(), "게시글이 누락되었습니다.");
        }
    }

    public void validatePostImagePath(String postImagePath) {
        Assert.notNull(postImagePath, "메인 이미지가 누락되었습니다.");
    }

}

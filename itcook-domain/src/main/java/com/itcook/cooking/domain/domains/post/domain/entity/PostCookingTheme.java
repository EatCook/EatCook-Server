package com.itcook.cooking.domain.domains.post.domain.entity;

import com.itcook.cooking.domain.domains.post.domain.entity.validator.PostValidator;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCookingTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cooking_theme_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CookingType cookingType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private PostCookingTheme(Long id, CookingType cookingType, Post post) {
        this.id = id;
        this.cookingType = cookingType;
        this.post = post;
    }

    public static List<PostCookingTheme> addPostCookingTheme(
            List<CookingType> cookingType, Post post, PostValidator postValidator
    ) {
        Set<CookingType> cookingTypes = new HashSet<>(cookingType);
        List<PostCookingTheme> postCookingThemeList = cookingTypes.stream()
                .map(dto -> PostCookingTheme.builder()
                        .cookingType(dto)
                        .post(post).build()).toList();
        postValidator.validatePostCookingTheme(postCookingThemeList);
        return postCookingThemeList;
    }

}

package com.itcook.cooking.domain.domains.post.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_process_id")
    private Long id;

    @Column(nullable = false)
    private String recipeWriting;

    @Column(nullable = false)
    private Integer stepNum;

    private String recipeProcessImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private RecipeProcess(String recipeWriting, Integer stepNum, String recipeProcessImagePath, Post post) {
        this.recipeWriting = recipeWriting;
        this.stepNum = stepNum;
        this.recipeProcessImagePath = recipeProcessImagePath;
        this.post = post;
    }


    public static RecipeProcess addRecipeProcess(Integer stepNum, String recipeWriting, Post post) {
        return RecipeProcess.builder()
                .stepNum(stepNum)
                .recipeWriting(recipeWriting)
                .post(post)
                .build();
    }

    public static RecipeProcess addRecipeProcessFromUpdate(Integer stepNum, String recipeWriting, String newFileExtension, Post post) {
        return RecipeProcess.builder()
                .stepNum(stepNum)
                .recipeWriting(recipeWriting)
                .recipeProcessImagePath(newFileExtension)
                .post(post)
                .build();
    }

    public void updateRecipeProcess(Integer stepNum, String recipeWriting, String fileExtension) {
        this.stepNum = stepNum;
        this.recipeWriting = recipeWriting;
        this.recipeProcessImagePath = fileExtension;
    }

    public void updateFileExtension(String recipeProcessImagePath) {
        this.recipeProcessImagePath = recipeProcessImagePath;
    }
}

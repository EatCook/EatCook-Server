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
import java.util.List;

import static com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeAddDto.RecipeProcessAddDto;

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
    public RecipeProcess(Long id, String recipeWriting, Integer stepNum, Post post) {
        this.id = id;
        this.recipeWriting = recipeWriting;
        this.stepNum = stepNum;
        this.post = post;
    }

    public static List<RecipeProcess> addRecipeProcess(List<RecipeProcessAddDto> recipeProcessAddDtoList, Post post) {
        return recipeProcessAddDtoList.stream()
                .map(dto -> RecipeProcess.builder()
                        .recipeWriting(dto.recipeWriting())
                        .stepNum(dto.stepNum())
                        .post(post)
                        .build()).toList();
    }

    public void updateRecipeProcess(RecipeProcess recipeProcess) {
        this.recipeWriting = recipeProcess.getRecipeWriting();
        this.stepNum = recipeProcess.getStepNum();
        this.recipeProcessImagePath = recipeProcess.getRecipeProcessImagePath();
        this.post = recipeProcess.getPost();
    }

    public void updateFileExtension(String recipeProcessImagePath) {
        this.recipeProcessImagePath = recipeProcessImagePath;
    }
}

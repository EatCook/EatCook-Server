package com.itcook.cooking.domain.domains.post.domain.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeAddDto;
import com.itcook.cooking.domain.domains.post.domain.entity.validator.PostValidator;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String recipeName; // 요리 제목
    private Integer recipeTime; // 요리 시간
    private String introduction; // 요리 소개글

    private String postImagePath; // 메인 이미지

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeProcess> recipeProcesses = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostCookingTheme> postCookingThemes = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "food_ingredients", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "ingredient_name")
    private List<String> foodIngredients; //재료

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "post_life_type", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "life_type")
    private List<LifeType> lifeTypes; //거주 형태

    @Enumerated(EnumType.STRING)
    private PostFlag postFlag;

    @Builder
    private Post(
            String recipeName, Integer recipeTime, String introduction,
            String postImagePath, Long userId, List<RecipeProcess> recipeProcesses,
            List<PostCookingTheme> postCookingThemes, List<String> foodIngredients,
            List<LifeType> lifeTypes, PostFlag postFlag
    ) {
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
        this.introduction = introduction;
        this.postImagePath = postImagePath;
        this.userId = userId;
        this.recipeProcesses = recipeProcesses;
        this.postCookingThemes = postCookingThemes;
        this.foodIngredients = foodIngredients;
        this.lifeTypes = lifeTypes;
        this.postFlag = postFlag;
    }

    /**
     * 레시피 등록
     */
    public static Post addPost(RecipeAddDto dto, PostValidator postValidator) {
        List<LifeType> lifeTypeList = new HashSet<>(dto.lifeTypes()).stream().toList();
        Post post = Post.builder()
                .recipeName(dto.recipeName())
                .recipeTime(dto.recipeTime())
                .introduction(dto.introduction())
                .userId(dto.userId())
                .foodIngredients(dto.foodIngredients())
                .lifeTypes(lifeTypeList)
                .postFlag(PostFlag.ACTIVATE)
                .build();
        postValidator.validateAdd(post);
        return post;
    }

    public void addRecipeProcess(List<RecipeProcess> recipeProcesses) {
        this.recipeProcesses = recipeProcesses;
    }

    public void updateCookingTheme(
            List<PostCookingTheme> postCookingThemes
    ) {
        this.postCookingThemes = postCookingThemes;
    }

    public void updatePost(
            String recipeName,
            Integer recipeTime,
            String introduction,
            List<String> foodIngredients,
            PostValidator postValidator
    ) {
        //TODO
//        postValidator.validateUpdate();
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
        this.introduction = introduction;
        this.foodIngredients = foodIngredients;
    }

    public void updatePostImagePath(String postImagePath, PostValidator postValidator) {
        postValidator.validatePostImagePath(postImagePath);
        this.postImagePath = postImagePath;
    }

    public void removePost() {
        this.postFlag = PostFlag.DISABLED;
    }

    public Boolean isAuthor(Long userId) {
        return Objects.equals(this.userId, userId);
    }
}

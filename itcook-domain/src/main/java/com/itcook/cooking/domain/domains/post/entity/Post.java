package com.itcook.cooking.domain.domains.post.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;

import java.util.List;
import javax.persistence.*;

import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ElementCollection
    @CollectionTable(name = "food_ingredients", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "ingredient_name")
    private List<String> foodIngredients; //재료

    @Enumerated(EnumType.STRING)
    private PostFlag postFlag;

    @Builder
    public Post(Long id, String recipeName, Integer recipeTime, String introduction, String postImagePath,
                Long userId, List<String> foodIngredients, PostFlag postFlag) {
        this.id = id;
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
        this.introduction = introduction;
        this.userId = userId;
        this.foodIngredients = foodIngredients;
        this.postImagePath = postImagePath;
        this.postFlag = postFlag;
    }

    public void updatePost(Post updateData) {
        this.recipeName = updateData.getRecipeName();
        this.recipeTime = updateData.getRecipeTime();
        this.introduction = updateData.getIntroduction();
        this.userId = updateData.getUserId();
        this.foodIngredients = updateData.getFoodIngredients();
        this.postImagePath = updateData.postImagePath;
    }

    public void updateFileExtension(String postImagePath) {
        this.postImagePath = postImagePath;
    }

    public void deletePost() {
        this.postFlag = PostFlag.DISABLED;
    }

}

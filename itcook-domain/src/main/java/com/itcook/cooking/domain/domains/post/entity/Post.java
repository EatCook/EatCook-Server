package com.itcook.cooking.domain.domains.post.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    private Integer likeCount; // 좋아요수


    private Long userId;

    @ElementCollection
    @CollectionTable(name = "food_ingredients", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "ingredient_name")
    private List<String> foodIngredients;

    @Builder
    public Post(Long id, String recipeName, Integer recipeTime, String introduction,
        Integer likeCount,
        List<String> foodIngredients) {
        this.id = id;
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
        this.introduction = introduction;
        this.likeCount = likeCount;
        this.foodIngredients = foodIngredients;
    }
}

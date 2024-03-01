package com.itcook.cooking.domain.domains.post.entity;

import static javax.persistence.FetchType.LAZY;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CookingTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cooking_theme_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CookingType cookingType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Long userId;

    @Builder
    public CookingTheme(Long id, Long userId, CookingType cookingType, Post post) {
        this.id = id;
        this.userId = userId;
        this.cookingType = cookingType;
        this.post = post;
    }

    public static CookingTheme createCookingTheme(Long userId, CookingType cookingType)  {
        return CookingTheme.builder()
            .userId(userId)
            .cookingType(cookingType)
            .build();
    }
}

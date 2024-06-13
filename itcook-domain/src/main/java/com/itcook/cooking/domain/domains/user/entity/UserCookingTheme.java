package com.itcook.cooking.domain.domains.user.entity;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCookingTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cooking_theme_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CookingType cookingType;

    private Long userId;

    @Builder
    private UserCookingTheme(Long id, CookingType cookingType, Long userId) {
        this.cookingType = cookingType;
        this.userId = userId;
    }

    private static UserCookingTheme create(Long userId, CookingType cookingType)  {
        return UserCookingTheme.builder()
            .userId(userId)
            .cookingType(cookingType)
            .build();
    }

    protected static List<UserCookingTheme> create(Long userId, List<CookingType> cookingTypes) {
        return cookingTypes.stream()
            .map(cookingType -> UserCookingTheme.create(userId, cookingType))
            .toList();
    }

    public String getCookingTypeName() {
        return cookingType.getCookingTypeName();
    }

}

package com.itcook.cooking.domain.domains.user.entity;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
public class UserCookingTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cooking_theme_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CookingType cookingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ItCookUser user;

    @Builder
    private UserCookingTheme(Long id, CookingType cookingType, ItCookUser user) {
        this.cookingType = cookingType;
        this.user = user;
    }

    private static UserCookingTheme create(ItCookUser user, CookingType cookingType)  {
        return UserCookingTheme.builder()
            .user(user)
            .cookingType(cookingType)
            .build();
    }

    protected static List<UserCookingTheme> create(ItCookUser user, List<CookingType> cookingTypes) {
        return cookingTypes.stream()
            .map(cookingType -> UserCookingTheme.create(user, cookingType))
            .toList();
    }

    protected String getCookingTypeName() {
        return cookingType.getCookingTypeName();
    }

    protected void addUser(ItCookUser user) {
        if (this.user != null) {
            this.user.getUserCookingThemes().remove(this);
        }
        this.user = user;
        user.getUserCookingThemes().add(this);
    }
}

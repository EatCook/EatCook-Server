package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import java.util.List;
import lombok.Builder;

@Builder
public record AddSignupServiceDto(
    String email,
    String fileExtension,
    String nickName,
    List<String> cookingType,
    String lifeType

) {
    public List<CookingType> toCookingTypes() {
        return this.cookingType.stream()
            .map(CookingType::getByName)
            .toList();
    }

    public ItCookUser toEntity() {
        return ItCookUser.builder()
            .email(email)
            .nickName(this.nickName)
            .lifeType(LifeType.getByName(this.lifeType))
            .build();
    }
}

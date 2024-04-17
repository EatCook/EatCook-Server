package com.itcook.cooking.domain.domains.user.service.dto;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import java.util.List;
import lombok.Builder;

@Builder
public record UserUpdateInterestCook(
    List<CookingType> cookingTypes,
    LifeType lifeType
) {

}

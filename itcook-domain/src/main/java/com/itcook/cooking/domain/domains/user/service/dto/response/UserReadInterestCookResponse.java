package com.itcook.cooking.domain.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import java.util.List;
import lombok.Builder;

@Builder
public record UserReadInterestCookResponse(
    String lifeType,
    List<String> cookingTypes
) {

    public static UserReadInterestCookResponse of(
        ItCookUser user
    ) {
        return UserReadInterestCookResponse.builder()
            .lifeType(user.getLifeTypeName())
            .cookingTypes(user.getCookingTypes())
            .build()
            ;
    }
}

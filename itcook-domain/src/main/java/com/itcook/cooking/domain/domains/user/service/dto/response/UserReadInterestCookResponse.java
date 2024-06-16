package com.itcook.cooking.domain.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import java.util.List;
import java.util.stream.Collectors;
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

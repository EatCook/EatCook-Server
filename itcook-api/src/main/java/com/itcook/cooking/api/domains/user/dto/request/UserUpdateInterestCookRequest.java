package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdateInterestCook;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.Size;

@Schema(name = "관심 요리 설정 요청")
public record UserUpdateInterestCookRequest(
    @Schema(description = "생활 유형", example = "다이어트만 n번째")
    String lifeType,
    @Size(max = 3, message = "최대 3개까지 선택 가능합니다.")
    @Schema(description = "요리 유형", example = "[\"한식\", \"중식\", \"일식\"]")
    List<String> cookingTypes
) {

    public UserUpdateInterestCook toServiceDto() {
        return UserUpdateInterestCook.builder()
            .lifeType(LifeType.getByName(lifeType))
            .cookingTypes(cookingTypes.stream().map(CookingType::getByName).toList())
            .build()
            ;
    }
}

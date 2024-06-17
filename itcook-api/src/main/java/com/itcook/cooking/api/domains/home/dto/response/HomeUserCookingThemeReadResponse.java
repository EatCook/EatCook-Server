package com.itcook.cooking.api.domains.home.dto.response;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "home read response")
public class HomeUserCookingThemeReadResponse {
    @Schema(description = "닉네임", example = "김잇쿡")
    private String nickName;
    @Schema(description = "관심요리 수", example = "2")
    private int size;

    @Schema(description = "관심 요리 정보", example = "{\"디저트\":\"DESERT\"}")
    private Map<String, String> userCookingTheme;

    @Schema(description = "생활 유형 정보", example = "{\"다이어트만 n번째\":\"DIET\"}")
    private Map<String, String> lifeType;

    public static HomeUserCookingThemeReadResponse of(ItCookUser itCookUser) {
        return HomeUserCookingThemeReadResponse.builder()
                .nickName(itCookUser.getNickName())
                .size(itCookUser.getUserCookingThemes().size())
                .userCookingTheme(getUserCookingTheme(itCookUser))
                .lifeType(getLifeType(itCookUser))
                .build();
    }

    private static Map<String, String> getUserCookingTheme(ItCookUser itCookUser) {
        return itCookUser.getUserCookingThemes().stream()
                .collect(Collectors.toMap(
                        theme -> theme.getCookingType().getCookingTypeName(),
                        theme -> theme.getCookingType().toString()
                ));
    }

    private static Map<String, String> getLifeType(ItCookUser itCookUser) {
        if (itCookUser.getLifeType() != null) {
            return Map.of(itCookUser.getLifeTypeName(), String.valueOf(itCookUser.getLifeType()));
        } else {
            return Map.of();
        }
    }

}

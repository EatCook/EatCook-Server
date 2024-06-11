package com.itcook.cooking.api.domains.home.dto.response;

import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Schema(description = "관심 요리 정보", example = "[\"한식\",\"일식\",\"야식\"]")
    private List<String> userCookingTheme;

    public static HomeUserCookingThemeReadResponse of(
            String nickName,
            List<UserCookingTheme> findUserCookingTheme) {
        return HomeUserCookingThemeReadResponse.builder()
                .nickName(nickName)
                .size(findUserCookingTheme.size())
                .userCookingTheme(findUserCookingTheme.stream()
                        .map(UserCookingTheme::getCookingTypeName)
                        .toList())
                .build();
    }
}

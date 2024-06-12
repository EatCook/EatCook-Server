package com.itcook.cooking.domain.domains.user.entity.dto;

import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import java.util.List;
import lombok.Builder;

@Builder
public record AddSignupDomainResponse(
    ImageUrlDto imageUrlDto,
    List<UserCookingTheme> userCookingThemes
) {

    public static AddSignupDomainResponse of(ImageUrlDto imageUrlDto, List<UserCookingTheme> userCookingThemes) {
        return AddSignupDomainResponse.builder()
            .imageUrlDto(imageUrlDto)
            .userCookingThemes(userCookingThemes)
            .build()
            ;
    }
}

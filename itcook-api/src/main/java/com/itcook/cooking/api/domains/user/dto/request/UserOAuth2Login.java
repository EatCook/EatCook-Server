package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import lombok.Builder;

@Builder
public record UserOAuth2Login(
    String email,
    ProviderType providerType
) {

}

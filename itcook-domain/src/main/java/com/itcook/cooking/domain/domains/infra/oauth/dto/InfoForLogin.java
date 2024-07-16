package com.itcook.cooking.domain.domains.infra.oauth.dto;

import lombok.Builder;

@Builder
public record InfoForLogin(
    String token,
    String email
) {

}

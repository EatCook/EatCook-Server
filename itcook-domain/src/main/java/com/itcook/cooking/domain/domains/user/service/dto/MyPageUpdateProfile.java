package com.itcook.cooking.domain.domains.user.service.dto;

import lombok.Builder;

@Builder
public record MyPageUpdateProfile(
    String email,
    String nickName
) {

}

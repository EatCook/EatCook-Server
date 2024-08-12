package com.itcook.cooking.api.domains.user.service.dto;

import lombok.Builder;

@Builder
public record SendEmailServiceDto(
    String email
) {

}

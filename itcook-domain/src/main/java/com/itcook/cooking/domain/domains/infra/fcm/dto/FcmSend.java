package com.itcook.cooking.domain.domains.infra.fcm.dto;

import lombok.Builder;

@Builder
public record FcmSend(
    Long targetUserId,
    String title,
    String body
) {

}

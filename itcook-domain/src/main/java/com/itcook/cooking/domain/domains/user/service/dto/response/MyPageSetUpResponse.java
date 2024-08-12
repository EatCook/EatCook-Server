package com.itcook.cooking.domain.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.domain.enums.ServiceAlertType;
import lombok.Builder;

@Builder
public record MyPageSetUpResponse(
    ServiceAlertType serviceAlertType,
    EventAlertType eventAlertType
) {

    public static MyPageSetUpResponse of(ItCookUser user) {
        return MyPageSetUpResponse.builder()
            .serviceAlertType(user.getServiceAlertType())
            .eventAlertType(user.getEventAlertType())
            .build();
    }

}

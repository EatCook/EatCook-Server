package com.itcook.cooking.domain.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.enums.ServiceAlertType;
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

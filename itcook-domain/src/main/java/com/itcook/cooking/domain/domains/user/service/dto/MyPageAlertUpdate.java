package com.itcook.cooking.domain.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.domain.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.domain.enums.ServiceAlertType;
import lombok.Builder;

@Builder
public record MyPageAlertUpdate(
    ServiceAlertType serviceAlertType,
    EventAlertType eventAlertType
) {

}

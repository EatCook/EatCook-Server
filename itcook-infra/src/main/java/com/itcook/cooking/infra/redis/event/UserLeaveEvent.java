package com.itcook.cooking.infra.redis.event;

import lombok.Builder;

@Builder
public record UserLeaveEvent(
    String email
){

}

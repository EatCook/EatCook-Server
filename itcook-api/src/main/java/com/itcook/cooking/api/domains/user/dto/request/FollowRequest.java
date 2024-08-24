package com.itcook.cooking.api.domains.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "팔로우 요청")
public class FollowRequest {

    @NotNull
    @Schema(description = "팔로우 유저 id", example = "1")
    private Long toUserId;
}

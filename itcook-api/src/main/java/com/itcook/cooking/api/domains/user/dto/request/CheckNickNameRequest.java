package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.CheckNickNameServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CheckNickNameRequest(
    @NotBlank(message = "닉네임을 입력해주세요")
    @Schema(description = "닉네임", example = "잇쿡")
    String nickName
) {

    public CheckNickNameServiceDto toServiceDto() {
        return CheckNickNameServiceDto.builder()
            .nickName(nickName)
            .build();
    }
}

package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.MyPageProfileImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Builder;

@Builder
@Schema(name = "마이페이지 프로필 이미지 변경 요청")
public record MyPageProfileChangeRequest(
    @Schema(description = "프로필 이미지 확장자명(필수값 X), 프로필 이미지 삭제시에는 null값을 보내주시면 됩니다"
        , example = "jpg")
    String fileExtension
) {

    public MyPageProfileImageDto toServiceDto(String email) {
        return MyPageProfileImageDto.builder()
            .email(email)
            .fileExtension(fileExtension)
            .build();
    }
}

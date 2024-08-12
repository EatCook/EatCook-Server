package com.itcook.cooking.api.domains.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Schema(name = "추가 회원가입 응답")
public class AddUserResponse {

    private Long userId;
    private String presignedUrl;

}

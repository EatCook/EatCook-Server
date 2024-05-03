package com.itcook.cooking.api.domains.user.dto.response;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Schema(name = "회원가입 응답")
public class UserResponse {

//    @Schema(description = "유저 id", example = "1")
    private Long id;
//    @Schema(description = "유저 email", example = "user1@gmail.com")
    private String email;
    public static UserResponse of(ItCookUser user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .build()
            ;
    }
}

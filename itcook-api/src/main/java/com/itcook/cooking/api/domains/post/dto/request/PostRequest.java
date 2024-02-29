package com.itcook.cooking.api.domains.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "follower response")
public class PostRequest {

    @Schema(description = "유저 email", example = "user@test.com")
    @Email(message = "이메일 형식에 맞게 입력해주세요")
    private String email;

}

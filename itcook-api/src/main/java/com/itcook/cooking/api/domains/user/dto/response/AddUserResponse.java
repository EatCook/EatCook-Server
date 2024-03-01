package com.itcook.cooking.api.domains.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddUserResponse {

    private Long userId;
    private String presignedUrl;

}

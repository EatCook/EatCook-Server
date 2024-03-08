package com.itcook.cooking.api.domains.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class S3Res {
    private Long userId;
    private String fileExtension;
}

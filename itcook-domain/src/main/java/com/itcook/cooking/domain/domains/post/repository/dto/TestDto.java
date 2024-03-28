package com.itcook.cooking.domain.domains.post.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestDto {

    private Long postId;
    private Long likeCount;

}

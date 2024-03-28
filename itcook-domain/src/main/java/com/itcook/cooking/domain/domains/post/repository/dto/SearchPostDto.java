package com.itcook.cooking.domain.domains.post.repository.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostDto {

    private Long postId;
    private String recipeName;
    private String introduction;
    private String imageFilePath;
    private Long likeCount;
    private String userNickName;

}

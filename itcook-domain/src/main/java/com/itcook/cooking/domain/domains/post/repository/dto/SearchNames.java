package com.itcook.cooking.domain.domains.post.repository.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchNames {

    private Long postId;
    private String recipeName;
    private String introduction;
    private String imageFilePath;
    private int likeCount;
    private String userNickName;

}

package com.itcook.cooking.domain.domains.post.dto;

import com.itcook.cooking.domain.domains.post.repository.dto.SearchNames;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFinalNames {


    private Long postId;
    private String recipeName;
    private String introduction;
    private String imageFilePath;
    private List<String> foodIngredients;
    private String userNickName;

    public static SearchFinalNames from(SearchNames searchNames, List<String> foodIngredients) {
        return SearchFinalNames.builder()
            .postId(searchNames.getPostId())
            .recipeName(searchNames.getRecipeName())
            .introduction(searchNames.getIntroduction())
            .imageFilePath(searchNames.getImageFilePath())
            .userNickName(searchNames.getUserNickName())
            .foodIngredients(foodIngredients)
            .build();

    }
}

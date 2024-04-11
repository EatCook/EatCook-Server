package com.itcook.cooking.api.domains.post.dto.recipe;

import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeReadDto {

    private Long postId;
    private String recipeName;
    private Integer recipeTime;
    private String introduction;
    private String postImagePath;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private Integer foodIngredientsCnt;
    private List<String> foodIngredients;

    private List<String> cookingType;

    private List<RecipeProcessReadDto> recipeProcess;

    private Long userId;
    private String nickName;
    private String profile;

    private Integer followerCount;
    private Integer likedCount;

    private Boolean followCheck;
    private Boolean likedCheck;
    private Boolean archiveCheck;
}

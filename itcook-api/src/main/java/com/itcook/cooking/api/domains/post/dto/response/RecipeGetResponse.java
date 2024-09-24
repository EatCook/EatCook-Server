package com.itcook.cooking.api.domains.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itcook.cooking.api.domains.post.dto.recipe.RecipeProcessGetResponse;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.RecipeDto;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "Recipe Response")
public class RecipeGetResponse {
    private Long writerUserId;
    private String writerUserEmail;
    private String writerProfile;
    private String writerNickName;

    private Long postId;
    private String recipeName;
    private Integer recipeTime;
    private String introduction;
    private String postImagePath;
    private List<String> foodIngredients;
    private List<String> lifeTypes;
    private List<String> cookingType;
    private List<RecipeProcessGetResponse> recipeProcess;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    private Long likedCount;
    private Boolean followCheck;
    private Boolean likedCheck;
    private Boolean archiveCheck;

    public static RecipeGetResponse of(
            RecipeDto dto,
            boolean followCheck
    ) {
        Post post = dto.getPost();
        ItCookUser itCookUser = dto.getItCookUser();
        List<String> lifeTypes = post.getLifeTypes().stream()
                .map(LifeType::getLifeTypeName).toList();
        List<String> cookingTypes = post.getPostCookingThemes().stream()
                .map(type -> type.getCookingType().getCookingTypeName())
                .toList();
        List<RecipeProcessGetResponse> recipeProcessGetResponses = RecipeProcessGetResponse
                .fromDto(post.getRecipeProcesses());

        return RecipeGetResponse.builder()

                .writerUserId(itCookUser.getId())
                .writerUserEmail(itCookUser.getEmail())
                .writerProfile(itCookUser.getProfile())
                .writerNickName(itCookUser.getNickName())
                .postId(post.getId())
                .recipeName(post.getRecipeName())
                .recipeTime(post.getRecipeTime())
                .introduction(post.getIntroduction())
                .postImagePath(post.getPostImagePath())
                .foodIngredients(post.getFoodIngredients())
                .lifeTypes(lifeTypes)
                .cookingType(cookingTypes)
                .recipeProcess(recipeProcessGetResponses)
                .lastModifiedAt(post.getLastModifiedAt())
                .likedCount(dto.getLikedCount())
                .followCheck(followCheck)
                .likedCheck(dto.getLikedCheck())
                .archiveCheck(dto.getArchiveCheck())
                .build();
    }
}

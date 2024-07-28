package com.itcook.cooking.api.domains.post.dto.request;

import com.itcook.cooking.api.domains.post.dto.recipe.RecipeProcessDto;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.domain.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "recipe update request")
public class RecipeUpdateRequest {

    @Schema(description = "email", example = "user@test.com")
    @NotBlank(message = "이메일을 입력해 주세요")
    private String email;

    @Schema(description = "postId", example = "1")
    @NotNull(message = "게시물 번호가 없습니다.")
    private Long postId;

    @Schema(description = "제목", example = "김밥 만들기")
    @NotBlank(message = "제목을 입력해 주세요")
    private String recipeName;
    @Schema(description = "조리 시간", example = "10")
    @NotNull(message = "조리 시간을 입력해 주세요")
    private Integer recipeTime;

    @Schema(description = "본문", example = "간단하게 만들 수 있어요")
    @NotBlank(message = "본문을 입력해 주세요")
    private String introduction;
    @Schema(description = "메인 이미지 확장자명", example = "jpg")
    @NotBlank(message = "메인 이미지 확장자 명이 빈 값입니다.")
    private String mainFileExtension;

    @Schema(description = "유저 id", example = "1")
    @NotNull(message = "유저 정보가 없습니다.")
    private Long userId;

    @Schema(description = "재료", example = "[\"김밥\",\"밥\"]")
    @NotEmpty(message = "재료를 입력해 주세요")
    private List<String> foodIngredients;

    @Schema(description = "테마", example = "[\"한식\",\"중식\"]")
    @NotEmpty(message = "테마 입력해 주세요")
    private List<String> cookingType;

    @Schema(description = "조리 과정", example = "[ {\n \"stepNum\": 1,\n \"recipeWriting\": \"밥을 준비해 주세요\",\n \"fileExtension\": \"jpeg\"\n },\n" +
            "    {\n \"stepNum\": 2,\n \"recipeWriting\": \"밥을 한 주먹 ~\",\n \"fileExtension\": \"default\"\n }\n" +
            "  ]")
    @NotEmpty(message = "조리 과정 번호가 없습니다.")
    private List<RecipeProcessDto> recipeProcess;

    public Post toPostDomain() {
        return Post.builder()
                .recipeName(recipeName)
                .recipeTime(recipeTime)
                .introduction(introduction)
                .userId(userId)
                .foodIngredients(foodIngredients)
                .postFlag(PostFlag.ACTIVATE).build();
    }

    public List<RecipeProcess> toRecipeProcessDomain(Post post) {
        return recipeProcess.stream()
                .map(process -> RecipeProcess.builder()
                        .stepNum(process.getStepNum())
                        .recipeWriting(process.getRecipeWriting())
                        .post(post)
                        .build())
                .collect(Collectors.toList());
    }

    public List<PostCookingTheme> toPostCookingThemeDomain(Post post) {
        return cookingType.stream()
                .map(name -> {
                    CookingType byName = CookingType.getByName(name);
                    return PostCookingTheme.builder().cookingType(byName).post(post).build();
                })
                .collect(Collectors.toList());
    }

}
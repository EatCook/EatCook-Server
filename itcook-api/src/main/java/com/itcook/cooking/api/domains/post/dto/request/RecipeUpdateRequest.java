package com.itcook.cooking.api.domains.post.dto.request;

import com.itcook.cooking.api.domains.post.dto.RecipeProcessDto;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.RecipeProcess;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "recipe update request")
public class RecipeUpdateRequest {

    @Schema(description = "email", example = "user@test.com")
    @NotNull(message = "이메일을 입력해 주세요")
    private String email;

    @Schema(description = "postId", example = "1")
    @NotNull(message = "게시물 번호가 없습니다.")
    private Long postId;

    @Schema(description = "제목", example = "김밥 만들기")
    @NotNull(message = "제목을 입력해 주세요")
    private String recipeName;
    @Schema(description = "조리 시간", example = "10")
    @NotNull(message = "조리 시간을 입력해 주세요")
    private Integer recipeTime;

    @Schema(description = "본문", example = "간단하게 만들 수 있어요")
    @NotNull(message = "본문을 입력해 주세요")
    private String introduction;

    @Schema(description = "유저 id", example = "1")
    private Long userId;

    @Schema(description = "재료", example = "[\"김밥\",\"밥\"]")
    @NotNull(message = "재료를 입력해 주세요")
    private List<String> foodIngredients;

    @Schema(description = "테마", example = "[\"한식\",\"중식\"]")
    @NotNull(message = "테마 입력해 주세요")
    private List<String> cookingType;

    @Schema(description = "조리 과정",
            example = "[\n {\n \"stepNum\": 1,\n \"recipeWriting\": \"밥을 준비해 주세요\",\n \"recipeProcessImagePath\": \"step1Image.jpeg\"\n},\n" +
                    "{\n \"stepNum\": 2,\n \"recipeWriting\": \"밥을 한 주먹 ~\",\n \"recipeProcessImagePath\": \"step2Image.jpeg\"\n}\n" +
                    "  ]")
    @NotNull(message = "조리 과정을 입력해 주세요")
    private List<RecipeProcessDto> recipeProcess;

    public Post toPostDomain() {
        return Post.builder()
                .id(postId)
                .recipeName(recipeName)
                .recipeTime(recipeTime)
                .introduction(introduction)
                .userId(userId)
                .foodIngredients(foodIngredients)
                .postFlag((byte) 0).build();
    }

    public List<RecipeProcess> toRecipeProcessDomain(Post post) {
        return recipeProcess.stream()
                .map(process -> RecipeProcess.builder()
                        .stepNum(process.getStepNum())
                        .recipeWriting(process.getRecipeWriting())
                        .recipeProcessImagePath(process.getRecipeProcessImagePath())
                        .post(post)
                        .build())
                .collect(Collectors.toList());
    }

}
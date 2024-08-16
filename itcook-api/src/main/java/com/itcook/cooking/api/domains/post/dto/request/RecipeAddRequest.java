package com.itcook.cooking.api.domains.post.dto.request;

import com.itcook.cooking.api.domains.post.service.dto.RecipeAddServiceDto;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.itcook.cooking.api.domains.post.service.dto.RecipeAddServiceDto.RecipeProcessAddServiceDto;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_FOOD_INGREDIENTS_MAX_SIZE;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_FOOD_INGREDIENTS_VALUE_MAX_SIZE;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_FOOD_INGREDIENTS_VALUE_MIN_SIZE;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_INTRODUCTION_MAX_SIZE;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_MAX_TIME;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_MIN_TIME;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_NAME_MAX_SIZE;
import static com.itcook.cooking.domain.common.constant.PostConstant.RECIPE_PROCESS_MIN_STEP_NUM;

/**
 * TODO
 * 레시피 제목 최대 최소 길이
 * 레시피 설명 최대 최소 길이 100자
 * 조리시간 1분 ~ 23시간 59초
 * 재료 최대 최소 길이
 * 재료 최대 입력 수
 * 쿠킹타입 최대, 최소 선택 수
 * lifetype 최대 최소 선택 수,
 * 조리과정 최대 등록 수
 * 레시피 설명 최대 최소 길이 100자
 */
@Schema(name = "쿡톡에 저장할 레시피 저장")
public record RecipeAddRequest(
        @NotBlank(message = "레시피 제목이 입력해주세요.")
        @Size(max = RECIPE_NAME_MAX_SIZE, message = "레시피 제목 최대 길이를 정해주세요.")
        String recipeName,

        @NotBlank(message = "레시피 소개글이 입력해주세요.")
        @Size(max = RECIPE_INTRODUCTION_MAX_SIZE, message = "레시피 제목 최대 길이를 초과하였습니다.")
        String mainIntroduction,

        @NotNull(message = "조리시간이 입력해주세요.")
        @Max(value = RECIPE_MAX_TIME, message = "최대 조리시간을 초과하였습니다.")
        @Min(value = RECIPE_MIN_TIME, message = "최소 조리시간을 정해주세요.")
        Integer recipeTime,

        @NotBlank(message = "메인이미지를 등록해주세요")
        String mainFileExtension,

        @NotEmpty(message = "재료를 입력해주세요")
        @Size(max = RECIPE_FOOD_INGREDIENTS_MAX_SIZE, message = "최대로 입력할 수 있는 재료 수를 초과하였습니다.")
        List<@Size(max = RECIPE_FOOD_INGREDIENTS_VALUE_MAX_SIZE,
                min = RECIPE_FOOD_INGREDIENTS_VALUE_MIN_SIZE,
                message = "재료에 대한 설명을 수정해주세요") String> foodIngredients,

        @NotEmpty(message = "요리 테마를 선택해주세요")
        @Schema(description = "KOREAN_FOOD(\"한식\") \n" +
                "    JAPANESE_FOOD(\"일식\")\n" +
                "    WESTERN_FOOD(\"양식\")\n" +
                "    CHINESE_FOOD(\"중식\")\n" +
                "    SIDE_DISH(\"반찬\")\n" +
                "    LATE_NIGHT_SNACK(\"야식\")\n" +
                "    DESERT(\"디저트\")\n" +
                "    BUNSIK(\"분식\")\n" +
                "    ASIAN_FOOD(\"아시안\")", example = "KOREAN_FOOD")
        List<CookingType> cookingType,
        @NotEmpty(message = "요리 유형을 선택해주세요")
        @Schema(description = "DIET(\"다이어트식\")\n" +
                "    HEALTH_DIET(\"건강식\")\n" +
                "    CONVENIENCE_STORE(\"편의점 요리\")\n" +
                "    DELIVERY_FOOD(\"배달음식 단골고객\")\n" +
                "    MEAL_KIT(\"밀키트 요리\")\n"
                , example = "DIET")
        List<LifeType> lifeType,

        @Valid
        @NotNull(message = "조리 과정을 입력해주세요")
        List<RecipeProcessAddRequest> recipeProcess
) {
    public record RecipeProcessAddRequest(
            @NotNull(message = "조리과정의 번호가 누락되었습니다.")
            @Min(value = RECIPE_PROCESS_MIN_STEP_NUM, message = "최소 1개 이상을 등록해주세요")
            Integer stepNum,

            @NotBlank(message = "조리과정의 설명을 입력해주세요.")
            @Size(max = RECIPE_INTRODUCTION_MAX_SIZE, message = "조리 설명 최대 길이를 초과하였습니다.")
            String subIntroduction,

            @NotBlank(message = "조리과정의 이미지를 등록해주세요")
            String fileExtension
    ) {
    }

    public RecipeAddServiceDto toServiceDto(String email) {
        return RecipeAddServiceDto.builder()
                .email(email)
                .recipeName(recipeName)
                .introduction(mainIntroduction)
                .recipeTime(recipeTime)
                .mainFileExtension(mainFileExtension)
                .foodIngredients(foodIngredients)
                .cookingType(cookingType)
                .lifeType(lifeType)
                .recipeProcess(toServiceDto())
                .build();
    }

    private List<RecipeProcessAddServiceDto> toServiceDto() {
        return recipeProcess.stream()
                .map(rp -> RecipeProcessAddServiceDto.builder()
                        .stepNum(rp.stepNum)
                        .recipeWriting(rp.subIntroduction)
                        .fileExtension(rp.fileExtension)
                        .build()).toList();
    }
}

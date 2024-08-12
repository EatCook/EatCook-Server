package com.itcook.cooking.api.domains.post.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PostSearchServiceDto {


    private Long lastId;
    private List<String> recipeNames;
    private List<String> ingredients;
    @Builder.Default
    private Integer size = 10;

}

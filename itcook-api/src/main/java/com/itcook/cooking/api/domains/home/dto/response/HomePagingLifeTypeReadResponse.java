package com.itcook.cooking.api.domains.home.dto.response;

import com.itcook.cooking.domain.domains.post.repository.dto.HomePostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "paging Post LifeType response")
public class HomePagingLifeTypeReadResponse {

    private boolean nextPageValid;
    private Long totalElements;
    private Integer totalPages;
    private List<HomePostDto> homePostDtoList;

    public static HomePagingInterestReadResponse of(
            List<HomePostDto> homePostDtoList,
            boolean nextPage, Long totalElements, int totalPages) {
        return HomePagingInterestReadResponse.builder()
                .homePostDtoList(homePostDtoList)
                .nextPageValid(nextPage)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }

}
package com.itcook.cooking.api.domains.home.dto.response;

import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeInterestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "paging Post Cooking response")
public class HomePagingInterestReadResponse {

    private boolean nextPageValid;
    private Long totalElements;
    private Integer totalPages;
    private List<HomeInterestDto> homeInterestDtoList;

    public static HomePagingInterestReadResponse of(
            Page<HomeInterestDto> homeInterestDtoPage
    ) {
        return HomePagingInterestReadResponse.builder()
                .nextPageValid(homeInterestDtoPage.hasNext())
                .totalElements(homeInterestDtoPage.getTotalElements())
                .totalPages(homeInterestDtoPage.getTotalPages())
                .homeInterestDtoList(homeInterestDtoPage.getContent())
                .build();
    }
}

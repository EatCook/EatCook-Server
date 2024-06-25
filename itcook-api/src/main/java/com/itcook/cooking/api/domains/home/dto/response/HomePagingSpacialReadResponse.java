package com.itcook.cooking.api.domains.home.dto.response;

import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeSpecialDto;
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
@Schema(name = "paging Post Spacial Response")
public class HomePagingSpacialReadResponse {

    private boolean nextPageValid;
    private Long totalElements;
    private Integer totalPages;
    private List<HomeSpecialDto> homeInterestDtoList;

    public static HomePagingSpacialReadResponse of(
            Page<HomeSpecialDto> postsByLifeTypeData
    ) {
        return HomePagingSpacialReadResponse.builder()
                .nextPageValid(postsByLifeTypeData.hasNext())
                .totalElements(postsByLifeTypeData.getTotalElements())
                .totalPages(postsByLifeTypeData.getTotalPages())
                .homeInterestDtoList(postsByLifeTypeData.getContent())
                .build();
    }
}

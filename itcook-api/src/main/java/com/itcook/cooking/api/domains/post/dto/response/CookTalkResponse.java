package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.api.domains.post.dto.cooktalk.CookTalkDto;
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
@Schema(name = "cooktalk response")
public class CookTalkResponse {

    private boolean nextPageValid;
    private Long totalElements;
    private Integer totalPages;
    private List<CookTalkDto> cookTalkDtoList;

    public static CookTalkResponse of(List<CookTalkDto> cookTalkDto, boolean nextPage, Long totalElements, int totalPages) {
        return CookTalkResponse.builder()
                .cookTalkDtoList(cookTalkDto)
                .nextPageValid(nextPage)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }

}

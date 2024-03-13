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

    private List<CookTalkDto> cookTalkDtoList;

    private boolean hasNextPage;
    private Long getTotalElements;

    public static CookTalkResponse of(List<CookTalkDto> cookTalkDto, boolean hasNextPage, Long getTotalElements) {
        return CookTalkResponse.builder()
                .cookTalkDtoList(cookTalkDto)
                .hasNextPage(hasNextPage)
                .getTotalElements(getTotalElements)
                .build();
    }

    public void cookTalkDtoListAdd(CookTalkDto cookTalkDto) {
        cookTalkDtoList.add(cookTalkDto);
    }

}

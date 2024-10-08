package com.itcook.cooking.api.global.dto;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNextPage
) {
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getNumberOfElements(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext());
    }

}

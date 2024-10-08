package com.itcook.cooking.api.domains.search.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RealTimeSearchWords {

    private final List<String> searchWords;

    public RealTimeSearchWords(List<String> searchWords) {
        this.searchWords = searchWords;
    }
}

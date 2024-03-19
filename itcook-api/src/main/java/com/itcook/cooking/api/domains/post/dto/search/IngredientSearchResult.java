package com.itcook.cooking.api.domains.post.dto.search;

import com.itcook.cooking.domain.domains.post.dto.SearchFinalNames;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientSearchResult {
    private String ingredientName;
    private List<SearchFinalNames> searchResults;

}

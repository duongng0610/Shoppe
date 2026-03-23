package com.e_cormerce.shoppe.dto.response.search;

import com.e_cormerce.shoppe.dto.common.search.CategorySuggestionDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SearchResponse {
    List<CategorySuggestionDTO> suggestions;
}

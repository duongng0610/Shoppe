package com.e_cormerce.shoppe.dto.common.search;

import com.e_cormerce.shoppe.enums.search.MatchType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CategorySuggestionDTO {
    String categoryId;
    String categoryName;
    MatchType matchType;
}

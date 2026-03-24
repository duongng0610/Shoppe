package com.e_cormerce.shoppe.dto.response.search;

import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchResponse {
  List<CategoryProjection> suggestions;
}

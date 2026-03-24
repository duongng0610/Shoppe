package com.e_cormerce.shoppe.dto.response.search;

import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchResultResponse {
  List<ProductCardResponse> productCards;
}

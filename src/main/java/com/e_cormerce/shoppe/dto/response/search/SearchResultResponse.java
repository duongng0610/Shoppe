package com.e_cormerce.shoppe.dto.response.search;

import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SearchResultResponse {
    List<ProductCardResponse> responses;
}

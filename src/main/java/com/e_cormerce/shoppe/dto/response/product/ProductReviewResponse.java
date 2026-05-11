package com.e_cormerce.shoppe.dto.response.product;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewResponse {
    List<String> images;
    String description;
    int rate;
    LocalDateTime createdAt;
}

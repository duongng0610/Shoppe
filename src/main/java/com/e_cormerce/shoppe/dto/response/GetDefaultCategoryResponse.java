package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.entity.product.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class GetDefaultCategoryResponse {
    List<Category> defaults;
}

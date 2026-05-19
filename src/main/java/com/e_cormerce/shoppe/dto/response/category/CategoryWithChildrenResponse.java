package com.e_cormerce.shoppe.dto.response.category;

import com.e_cormerce.shoppe.projection.category.CategoryProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryWithChildrenResponse {
    String id;
    String val;
    String thumbnail;
    List<CategoryProjection> children;
}

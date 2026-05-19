package com.e_cormerce.shoppe.dto.response.category;

import com.e_cormerce.shoppe.projection.category.CategoryProjection;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

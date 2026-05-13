package com.e_cormerce.shoppe.dto.response.category;

import java.util.Date;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDetailResponse {
  String id;
  String val;
  String thumbnail;
  boolean deleted;
  Date createdAt;
  String parentVal;
}

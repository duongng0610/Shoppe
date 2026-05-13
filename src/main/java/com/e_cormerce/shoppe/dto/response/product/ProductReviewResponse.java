package com.e_cormerce.shoppe.dto.response.product;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

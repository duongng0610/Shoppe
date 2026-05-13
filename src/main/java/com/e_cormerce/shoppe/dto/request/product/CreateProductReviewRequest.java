package com.e_cormerce.shoppe.dto.request.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductReviewRequest {
  List<@NotBlank String> images;

  @NotBlank(message = "content is required")
  String description;

  @Min(value = 1, message = "Rating must be from 1 to 5")
  @Max(value = 5, message = "Rating must be from 1 to 5")
  int rate;

  @NotBlank(message = "order is required")
  String orderId;
}

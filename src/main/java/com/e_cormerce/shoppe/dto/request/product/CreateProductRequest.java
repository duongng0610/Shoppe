package com.e_cormerce.shoppe.dto.request.product;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {

  @NotBlank(message = "name is required")
  String name;

  @NotBlank(message = "description is required")
  String description;

  @NotBlank(message = "thumbnail is required")
  String thumbnailUrl;

  @NotNull(message = "originPrice is required")
  @Positive(message = "originPrice must be greater than 0")
  BigDecimal originPrice;

  @NotNull(message = "hasVariant is required")
  Boolean hasVariant;

  @NotNull(message = "totalQuantity is required")
  @Positive(message = "totalQuantity must be greater than 0")
  int totalQuantity;

  @NotBlank(message = "categoryId is required")
  String categoryId;

  @NotNull(message = "discount is required")
  @NumberFormat
  float discountPercentage;

  List<String> extraImageUrls;

  /**
   * @Valid bật nested validation: nếu có trường types sẽ vào types lấy field và so khớp với
   * validation
   */
  @Valid List<TypeDto> types;

  /**
   * @Valid bật nested validation: nếu có trường types sẽ vào types lấy field và so khớp với
   * validation
   */
  @Valid List<VariantRequest> variantRequests;
}

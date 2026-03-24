package com.e_cormerce.shoppe.dto.request.admin;

import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApproveProductsRequest {
  List<String> productIds;
}

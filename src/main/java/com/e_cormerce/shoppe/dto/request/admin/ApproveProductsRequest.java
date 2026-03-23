package com.e_cormerce.shoppe.dto.request.admin;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApproveProductsRequest {
    List<String> productIds;
}

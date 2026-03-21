package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.dto.common.CategoryDTO;
import com.e_cormerce.shoppe.dto.common.SellerDTO;
import com.e_cormerce.shoppe.dto.common.TypeResponse;
import com.e_cormerce.shoppe.dto.common.VariantDetailResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetProductDetailResponse {
    String id;
    String name;
    String description;
    CategoryDTO category;
    SellerDTO seller;
    String thumbnail;
    List<String> extraImages;
    List<VariantDetailResponse> variants;
    List<TypeResponse> types;
}

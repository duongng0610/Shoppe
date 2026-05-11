package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.response.product.ProductReviewResponse;
import com.e_cormerce.shoppe.entity.product.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {
    @Mapping(target = "images", ignore = true)
    ProductReviewResponse toResponse(ProductReview productReview);
}

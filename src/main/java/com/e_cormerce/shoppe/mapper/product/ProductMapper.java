package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.common.ProductDTO;
import com.e_cormerce.shoppe.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE //avoid waring causes by not match source to target
)
public interface ProductMapper {
    @Mapping(source = "discount_percentage", target = "discountPercentage")
    @Mapping(source = "total_quantity", target = "totalQuantity")
    ProductDTO
    toProductDTO(Product product);
}

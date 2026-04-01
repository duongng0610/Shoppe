package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.response.product.BaseProductResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy =
        ReportingPolicy.IGNORE // avoid waring causes by not match source to target
    )
public interface ProductMapper {
  ProductCardResponse toProductDTO(Product product);

  BaseProductResponse toBaseProductDTO(Product product);
}

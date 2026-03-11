package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.common.ProductDTO;
import com.e_cormerce.shoppe.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(target = "shopName", ignore = true)
  @Mapping(target = "categoryName", ignore = true)
  ProductDTO toProductDTO(Product product);
}

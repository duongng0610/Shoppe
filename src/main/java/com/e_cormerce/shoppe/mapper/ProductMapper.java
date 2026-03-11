package com.e_cormerce.shoppe.mapper;

import com.e_cormerce.shoppe.dto.common.ProductDTO;
import com.e_cormerce.shoppe.entity.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductDTO toProductDTO(Product product);
}

package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.common.CategoryDTO;
import com.e_cormerce.shoppe.entity.product.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDTO toCategoryDTO(Category categoryMapper);
}

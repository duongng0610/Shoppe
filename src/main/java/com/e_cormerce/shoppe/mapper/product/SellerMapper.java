package com.e_cormerce.shoppe.mapper.product;

import com.e_cormerce.shoppe.dto.common.SellerDTO;
import com.e_cormerce.shoppe.entity.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {
  SellerDTO toSellerDTO(User seller);
}

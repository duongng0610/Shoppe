package com.e_cormerce.shoppe.mapper.shopping_cart;

import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartItemDto;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.mapper.product.VariantMapper;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {VariantMapper.class})
public interface ShoppingCartItemMapper {
    ShoppingCartItemDto toDto(ShoppingCartItem item);
}

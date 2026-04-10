package com.e_cormerce.shoppe.mapper.order;

import com.e_cormerce.shoppe.dto.common.order.OrderDetailDto;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.mapper.address.AddressMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailsMapper {
  AddressMapper addressMapper;

  public OrderDetailDto toResponse(Order order) {
    return OrderDetailDto.builder()
        .id(order.getId())
        .productName(order.getProductName())
        .variantThumbnail(order.getVariant().getThumbnail())
        .clientUsername(order.getClient().getUsername())
        .sellerUsername(order.getSeller().getUsername())
        .quantity(order.getQuantity())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus().toString())
        .shippingAddress(addressMapper.toDto(order.getShippingAddress()))
        .shippingPhoneNumber(order.getShippingPhoneNumber())
        .createdAt(order.getCreatedAt())
        .build();
  }
}

package com.e_cormerce.shoppe.mapper.order;

import com.e_cormerce.shoppe.dto.common.order.OrderDetailDto;
import com.e_cormerce.shoppe.dto.response.order.OrderDto;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {AddressMapper.class}
)

public interface OrderMapper {
    OrderDto toOrderDto(Order order);

    @Mapping(target = "clientUsername", source = "client.username")
    @Mapping(target = "sellerUsername", source = "seller.username")
    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    OrderDetailDto toResponse(Order order);
}

package com.e_cormerce.shoppe.mapper.order;

import com.e_cormerce.shoppe.dto.response.order.OrderDto;
import com.e_cormerce.shoppe.entity.order.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toOrderDto(Order order);
}

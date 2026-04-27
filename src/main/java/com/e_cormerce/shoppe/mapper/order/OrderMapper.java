package com.e_cormerce.shoppe.mapper.order;

import com.e_cormerce.shoppe.dto.common.order.OrderDetailDto;
import com.e_cormerce.shoppe.dto.response.order.OrderDto;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class})
public interface OrderMapper {
  OrderDto toOrderDto(Order order);

  OrderDetailDto toOrderDetailResponse(Order order);
}

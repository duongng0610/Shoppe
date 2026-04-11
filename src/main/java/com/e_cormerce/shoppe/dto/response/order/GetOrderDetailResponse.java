package com.e_cormerce.shoppe.dto.response.order;

import com.e_cormerce.shoppe.dto.common.order.OrderDetailDto;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetOrderDetailResponse {
  List<OrderDetailDto> orderDetails;
}

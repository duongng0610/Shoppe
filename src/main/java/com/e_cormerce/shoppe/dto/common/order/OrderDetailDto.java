package com.e_cormerce.shoppe.dto.common.order;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.common.user.UserDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderDetailDto {
  String id;
  String productName;
  String variantThumbnail;
  UserDto client;
  UserDto seller;
  Integer quantity;
  BigDecimal totalPrice;
  String status;
  AddressDto shippingAddress;
  String shippingPhoneNumber;
  LocalDateTime createdAt;
}

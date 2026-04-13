package com.e_cormerce.shoppe.dto.response.payment;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.response.order.OrderDto;
import com.e_cormerce.shoppe.enums.transaction.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTransactionDto {
  OrderDto order;
  UserDto user;
  TransactionStatus status;
  LocalDateTime transactionDate;
  BigDecimal amount;
}

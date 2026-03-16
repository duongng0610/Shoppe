package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.order.OrderTrackingStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class OrderTracking {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(name = "ship_cost", precision = 15, scale = 2, nullable = false)
  BigDecimal shipCost;

  @Column(name = "required_date", nullable = false)
  LocalDate requiredDate;

  @Column(name = "created_at", nullable = false)
  LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "ENUM('PENDING', 'FAILED', 'SUCCEED')", nullable = false)
  OrderTrackingStatus status;

  @ManyToOne
  @JoinColumn(name = "shipper_id", nullable = false)
  User shipper;

  @ManyToOne
  @JoinColumn(name = "order_tracking_id", nullable = false)
  OrderTracking orderTracking;
}

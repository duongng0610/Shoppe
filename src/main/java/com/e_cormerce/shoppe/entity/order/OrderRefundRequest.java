package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.order.OrderRefundStatus;
import com.e_cormerce.shoppe.enums.order.OrderRefundType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "order_refund_requests",
    indexes = {@Index(name = "idx_order_refund_requests_seller", columnList = "seller_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRefundRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false, columnDefinition = "TEXT")
  String reason;

  @Column(name = "refund_amount", nullable = false)
  BigDecimal refundAmount;

  @Column(name = "created_at", nullable = false)
  LocalDateTime createdAt;

  @Column(name = "resolved_at")
  LocalDateTime resolvedAt;

  @Enumerated(EnumType.STRING)
  @Column(
      name = "refund_type",
      columnDefinition = "ENUM('CLIENT_REFUND', 'SHIPPER_REFUND')",
      nullable = false)
  OrderRefundType refundType;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "ENUM('PENDING', 'REJECTED', 'APPROVED')", nullable = false)
  OrderRefundStatus status;

  @ManyToOne
  @JoinColumn(name = "requester_id")
  User requester;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  User seller;

  @ManyToOne
  @JoinColumn(name = "order_id")
  Order order;
}

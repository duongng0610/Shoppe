package com.e_cormerce.shoppe.entity.client;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "client_stats")
@SQLDelete(sql = "UPDATE client_stats SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class ClientStat {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @ManyToOne
  @MapsId
  @JoinColumn(name = "id", nullable = false)
  User client;

  @Column(nullable = false, name = "total_orders", columnDefinition = "int default 0")
  int totalOrders;

  @Column(nullable = false, name = "pending_orders", columnDefinition = "int default 0")
  int pendingOrders;

  @Column(nullable = false, name = "approved_orders", columnDefinition = "int default 0")
  int approvedOrders;

  @Column(nullable = false, name = "shipping_orders", columnDefinition = "int default 0")
  int shippingOrders;

  @Column(nullable = false, name = "cancelled_orders", columnDefinition = "int default 0")
  int cancelledOrders;

  @Column(nullable = false, name = "completed_orders", columnDefinition = "int default 0")
  int completedOrders;

  @Column(nullable = false, name = "refund_requested_orders", columnDefinition = "int default 0")
  int refundRequestedOrders;

  @Column(nullable = false, name = "refunded_orders", columnDefinition = "int default 0")
  int refundedOrders;

  @Column(nullable = false, name = "failed_delivery_orders", columnDefinition = "int default 0")
  int failedDeliveryOrders;

  @Column(name = "total_spent", precision = 15, scale = 2, columnDefinition = "decimal default 0")
  BigDecimal totalSpent;

  @CreationTimestamp
  @Column(
      name = "created_at",
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}

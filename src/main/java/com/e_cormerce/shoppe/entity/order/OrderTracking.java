package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.order.OrderTrackingStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "order_trackings",
    indexes = {
      @Index(name = "idx_order_trackings_shipper", columnList = "shipper_id"),
      @Index(name = "idx_order_trackings_order_tracking", columnList = "order_tracking_id")
    })
@SQLDelete(sql = "UPDATE order_trackings SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class OrderTracking {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(name = "ship_cost", precision = 15, scale = 2, nullable = false)
  BigDecimal shipCost;

  @Column(name = "required_date", nullable = false)
  LocalDate requiredDate;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
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

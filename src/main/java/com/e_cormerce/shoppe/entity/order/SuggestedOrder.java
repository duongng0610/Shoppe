package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "suggested_orders",
    indexes = {
      @Index(name = "idx_shipper", columnList = "shipper_id"),
      @Index(name = "idx_order", columnList = "order_id")
    })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE suggested_orders SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class SuggestedOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(name = "shipping_cost", precision = 15, scale = 2, nullable = false)
  BigDecimal shippingCost;

  @Column(name = "required_date", nullable = false)
  LocalDate requiredDate;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  Order order;

  @ManyToOne
  @JoinColumn(name = "shipper_id", nullable = false)
  User shipper;
}

package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.Address;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "orders",
    indexes = {@Index(name = "idx_orders_status", columnList = "status")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Enumerated(EnumType.STRING)
  @Column(
      columnDefinition =
          "ENUM('PENDING','ACCEPTED','CANCELLED_BY_CLIENT',"
              + "'CANCELLED_BY_SHIPPER','CANCELLED_BY_SELLER','SHIPPING','ARRIVED',"
              + "'PAID','RETURN_REQUEST_BY_CLIENT','RETURN_REQUEST_BY_SHIPPER','RETURNED','BOMB')",
      nullable = false)
  OrderStatus status;

  @Column(name = "price_each", nullable = false)
  double priceEach;

  @Column(nullable = false)
  int quantity;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "variant_id", nullable = false)
  Variant variant;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  User client;

  @ManyToOne
  @JoinColumn(name = "shipping_address_id", nullable = false)
  Address shipping_address;
}

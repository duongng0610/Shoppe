package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.Address;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "order_tracking_locations",
    indexes = {@Index(name = "idx_order", columnList = "order_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTrackingLocation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  boolean isLastUpdate = true;
  LocalDate created_at;

  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false)
  Address address;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  Order order;
}

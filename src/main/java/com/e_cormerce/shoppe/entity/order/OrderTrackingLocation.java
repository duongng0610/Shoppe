package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.Address;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "UPDATE order_tracking_locations SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class OrderTrackingLocation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(name = "is_last_update")
  boolean isLastUpdate = true;

  @Column(name = "created_at")
  @CreationTimestamp
  LocalDate createdAt;

  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false)
  Address address;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  Order order;
}

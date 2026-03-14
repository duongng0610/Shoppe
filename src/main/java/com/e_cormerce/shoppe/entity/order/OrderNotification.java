package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "order_notifications",
    indexes = {@Index(name = "idx_to", columnList = "to_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderNotification {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String title;

  @Column(nullable = false)
  String message;

  @Column(name = "created_at", nullable = false)
  LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "from_id", nullable = false)
  User from;

  @ManyToOne
  @JoinColumn(name = "to_id", nullable = false)
  User to;
}

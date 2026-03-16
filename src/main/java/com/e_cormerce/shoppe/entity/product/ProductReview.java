package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "product_reviews",
    indexes = {
      @Index(name = "idx_product", columnList = "product_id"),
      @Index(name = "idx_client", columnList = "client_id")
    })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReview {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  int rate;

  @Column(columnDefinition = "TEXT", nullable = false)
  String description;

  @Column(name = "created_at", nullable = false)
  LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  User client;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  Product product;
}

package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "shopping_cart_items",
    indexes = {@Index(name = "idx_shopping_cart_items_user", columnList = "client_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  int quantity;

  @Column(name = "price_each", precision = 15, scale = 2, nullable = false)
  BigDecimal priceEach;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(name = "is_variant", columnDefinition = "boolean default false")
  boolean isVariant;

  @ManyToOne
  @JoinColumn(name = "variant_id")
  Variant variant;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  Product product;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private User client;
}

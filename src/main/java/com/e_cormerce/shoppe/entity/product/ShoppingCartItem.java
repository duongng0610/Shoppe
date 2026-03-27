package com.e_cormerce.shoppe.entity.product;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "shopping_cart_items",
    indexes = {@Index(name = "idx_shopping_cart_items_cart", columnList = "shopping_cart_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE shopping_cart_items SET deleted=true where id=?")
@Where(clause = "deleted = false")
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
  @JoinColumn(name = "variant_id", nullable = false)
  Variant variant;

  @ManyToOne
  @JoinColumn(name = "shopping_cart_id", nullable = false)
  ShoppingCart shoppingCart;
}

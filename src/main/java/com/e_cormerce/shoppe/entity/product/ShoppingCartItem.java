package com.e_cormerce.shoppe.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    Variant variant;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    ShoppingCart shoppingCart;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Date updatedAt;
}

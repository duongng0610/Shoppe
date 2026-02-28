package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "shopping_cart_items",
        indexes = {@Index(name = "idx_user", columnList = "user_id")})
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

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal price_each;

    @Column(columnDefinition = "boolean default false")
    boolean is_deleted;

    @Column(columnDefinition = "boolean default false")
    boolean is_Variant;

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

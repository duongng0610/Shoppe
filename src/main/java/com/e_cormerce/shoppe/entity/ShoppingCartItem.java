package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    int quantity;
    double price_each;
    boolean has_variant;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    Variant variant;
    

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

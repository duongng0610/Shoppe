package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int quantity;
    double price_each;
    boolean is_deleted = false;
    boolean is_Variant;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    Variant variant;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

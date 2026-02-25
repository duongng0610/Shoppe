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

    @ManyToOne
    User user;

}

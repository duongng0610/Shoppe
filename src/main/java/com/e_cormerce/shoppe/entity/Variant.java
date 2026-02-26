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

public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    double price;
    int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

}

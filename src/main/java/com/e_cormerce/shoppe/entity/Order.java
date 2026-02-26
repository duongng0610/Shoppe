package com.e_cormerce.shoppe.entity;

import com.e_cormerce.shoppe.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Status status;
    Date created_at;
    Date updated_at;
    double price_each;
    int quantity;

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

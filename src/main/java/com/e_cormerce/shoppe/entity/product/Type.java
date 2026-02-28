package com.e_cormerce.shoppe.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String val;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    // owner side
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}

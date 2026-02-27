package com.e_cormerce.shoppe.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "users",
        indexes = {@Index(name = "idx_product", columnList = "product")}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String thumbnail;
    String description;

    double origin_price;
    float discount_percentage;
    double total_quantity;

    boolean has_Variant;

    // owner side
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    @Nullable
    Category category;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Type> types;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Variant> variants;

}

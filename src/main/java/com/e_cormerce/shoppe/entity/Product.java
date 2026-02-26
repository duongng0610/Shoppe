package com.e_cormerce.shoppe.entity;

import com.e_cormerce.shoppe.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "products",
        indexes = {@Index(name = "idx_category", columnList = "category_id")}
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
    ProductStatus product_status;

    boolean has_Variant;

    // owner side
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    Category category;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Type> types;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Variant> variants;

}

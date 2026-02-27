package com.e_cormerce.shoppe.entity;

import com.e_cormerce.shoppe.enums.ProductStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "products",
        indexes = {@Index(name = "idx_category", columnList = "category_id"),
                @Index(name = "idx_name", columnList = "name")
        }
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
    ProductStatus status;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    // owner side
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    Category category;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    @Column(nullable = true)
    Set<ProductType> types;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    @Column(nullable = true)
    Set<Variant> variants;

}

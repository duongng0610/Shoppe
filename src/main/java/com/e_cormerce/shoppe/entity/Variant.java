package com.e_cormerce.shoppe.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "variants", indexes = {@Index(name = "idx_product", columnList = "product_id")})
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    double price;
    int quantity;
    double discount_percentage;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;


    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    @Nullable
    Set<VariantValue> variantValues;

}

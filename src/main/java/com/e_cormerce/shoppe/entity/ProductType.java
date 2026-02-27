package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product_types", indexes = {@Index(name = "idx_product", columnList = "product_id")})
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String val;

    // inverse side
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    // owner side
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<TypeValue> typeValues;
}

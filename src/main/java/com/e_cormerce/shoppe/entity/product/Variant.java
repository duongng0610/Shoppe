package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal price;

    @Column(nullable = false)
    int quantity;
    double discount_percentage;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    Product product;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    @Nullable
    Set<VariantValue> variantValues;

}

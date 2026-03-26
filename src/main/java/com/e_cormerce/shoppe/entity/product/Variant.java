package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "variants",
        indexes = {@Index(name = "idx_product", columnList = "product_id")})
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal price;

    @Column(nullable = false)
    String thumbnail;

    @Column(nullable = false)
    double quantity;

    @Column(name = "discount_percentage")
    double discountPercentage;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;


    @Column(name = "sold_quantity", nullable = false)
    long soldQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    Product product;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Nullable
    List<VariantValue> variantValues;
}

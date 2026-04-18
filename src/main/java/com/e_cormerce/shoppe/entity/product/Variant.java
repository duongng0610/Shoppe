package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
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
@SQLDelete(sql = "UPDATE type_value SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal price;

    @Column(nullable = false)
    String thumbnail;

    @Column(nullable = false)
    int quantity;

    @Column(name = "quantity_sold")
    int quantitySold;

    @Column(name = "discount_percentage", columnDefinition = "float default 0")
    double discountPercentage;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @Column(name = "is_default", columnDefinition = "boolean default false")
    boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    Product product;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<VariantValue> variantValues;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Date updatedAt;
}

package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_category", columnList = "category_id"),
                @Index(name = "idx_seller", columnList = "seller_id"),
                @Index(name = "idx_seller_status", columnList = "seller_id, status")
        })
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

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String thumbnail;

    @Column(nullable = false, columnDefinition = "TEXT")
    String description;

    @Column(name = "origin_price", precision = 15, scale = 2, nullable = false)
    BigDecimal originPrice;

    @Column(name = "discount_percentage")
    float discountPercentage;

    @Column(name = "total_quantity", nullable = false)
    int totalQuantity;

    @Column(name = "has_variant", nullable = false)
    boolean hasVariant;

    @Enumerated(EnumType.STRING)
    @Column(
            columnDefinition = "ENUM('BANNED', 'PENDING', 'APPROVED', 'HIDDEN')") // , nullable = false)
    ProductStatus status = ProductStatus.PENDING;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    // owner side
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Type> types;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Variant> variants;

    // inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductExtraImage> productExtraImages;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;
}

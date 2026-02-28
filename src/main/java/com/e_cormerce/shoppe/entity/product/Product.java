package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products",
        indexes = {
                @Index(name = "idx_category", columnList = "category_id"),
                @Index(name = "idx_seller", columnList = "seller_id"),
                @Index(name = "idx_seller_status", columnList = "seller_id, status")
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

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String thumbnail;

    @Column(nullable = false, columnDefinition = "TEXT")
    String reason;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal origin_price;

    float discount_percentage;

    @Column(nullable = false)
    double total_quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ProductStatus product_status;

    LocalDateTime created_at;

    boolean has_Variant = false;

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

    //inverse side
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductExtraImage> productExtraImages;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

}

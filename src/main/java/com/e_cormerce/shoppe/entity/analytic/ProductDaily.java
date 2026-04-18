package com.e_cormerce.shoppe.entity.analytic;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_daily",
        indexes = {
                @Index(name = "idx_product", columnList = "product_id,date"),
                @Index(name = "idx_seller_product", columnList = "seller_id,product_id,date"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(ProductDailyId.class)
public class ProductDaily {

    @Id
    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(name = "total_views", nullable = false, columnDefinition = "int default 0")
    int totalViews;

    @Column(name = "total_orders", nullable = false, columnDefinition = "int default 0")
    int totalOrders;

    @Column(name = "total_units_sold", nullable = false, columnDefinition = "int default 0")
    int totalUnitsSold;

    @Column(name = "total_revenue", precision = 15, scale = 2, nullable = false)
    BigDecimal totalRevenue;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Id
    @Column(name = "product_id", nullable = false)
    String productId;


    @Column(name = "seller_id", nullable = false)
    String sellerId;

    @Column(name = "category_id", nullable = false)
    String categoryId;

}

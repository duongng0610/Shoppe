package com.e_cormerce.shoppe.entity.analytic;


import com.e_cormerce.shoppe.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_daily",
        indexes = {
                @Index(name = "idx_product", columnList = "product_id"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDaily {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(name = "total_views", nullable = false, columnDefinition = "int default 0")
    Integer totalViews;

    @Column(name = "total_orders", nullable = false, columnDefinition = "int default 0")
    Integer totalOrders;

    @Column(name = "total_units_sold", nullable = false, columnDefinition = "int default 0")
    Integer totalUnitsSold;


    @Column(name = "total_revenue", precision = 15, scale = 2, nullable = false)
    BigDecimal totalRevenue;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

}

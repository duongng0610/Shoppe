package com.e_cormerce.shoppe.entity.analytic;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_daily_sales",
        indexes = {
                @Index(name = "idx_product", columnList = "product_id"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemDaily {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "view_date", nullable = false)
    LocalDate viewDate;

    @Column(name = "total_revenue", precision = 15, scale = 2, nullable = false)
    BigDecimal totalRevenue;

    @Column(name = "total_visits", nullable = false, columnDefinition = "int default 0")
    Integer totalVisitors;

    @Column(name = "total_new_sellers", nullable = false, columnDefinition = "int default 0")
    Integer totalNewSellers;

    @Column(name = "total_new_clients", nullable = false, columnDefinition = "int default 0")
    Integer totalNewClients;

    @Column(name = "total_searches", nullable = false, columnDefinition = "int default 0")
    Integer totalSearches;

    @Column(name = "total_new_products", nullable = false, columnDefinition = "int default 0")
    Integer totalNewProducts;

    @Column(name = "total_new_orders", nullable = false, columnDefinition = "int default 0")
    Integer totalNewOrders;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;


}

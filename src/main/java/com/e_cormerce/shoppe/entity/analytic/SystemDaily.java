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
        name = "system_daily",
        indexes = {
                @Index(name = "idx_date", columnList = "date"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemDaily {

    @Id
    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(name = "total_revenue", precision = 15, scale = 2, nullable = false)
    BigDecimal totalRevenue;

    @Column(name = "total_visits", nullable = false, columnDefinition = "int default 0")
    int totalVisitors;

    @Column(name = "total_new_sellers", nullable = false, columnDefinition = "int default 0")
    int totalNewSellers;

    @Column(name = "total_new_clients", nullable = false, columnDefinition = "int default 0")
    int totalNewClients;

    @Column(name = "total_searches", nullable = false, columnDefinition = "int default 0")
    int totalSearches;

    @Column(name = "total_new_products", nullable = false, columnDefinition = "int default 0")
    int totalNewProducts;

    @Column(name = "total_new_orders", nullable = false, columnDefinition = "int default 0")
    int totalNewOrders;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;


}

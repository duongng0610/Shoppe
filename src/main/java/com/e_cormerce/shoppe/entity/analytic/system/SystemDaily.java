package com.e_cormerce.shoppe.entity.analytic.system;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

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


    @Column(name = "total_visits", nullable = false, columnDefinition = "int default 0")
    int totalVisits;

    @Column(name = "total_new_sellers", nullable = false, columnDefinition = "int default 0")
    int totalNewSellers;

    @Column(name = "total_new_clients", nullable = false, columnDefinition = "int default 0")
    int totalNewClients;

    @Column(name = "total_transaction_count", nullable = false, columnDefinition = "int default 0")
    int totalTransactionCount;

    @Column(name = "total_transaction_amount", nullable = false, columnDefinition = "int default 0")
    int totalTransactionAmount;


    @Column(name = "total_new_products", nullable = false, columnDefinition = "int default 0")
    int totalNewProducts;

    @Column(name = "total_new_orders", nullable = false, columnDefinition = "int default 0")
    int totalNewOrders;


    @Column(
            name = "created_at",
            nullable = false,
            updatable = false,
            insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;


}

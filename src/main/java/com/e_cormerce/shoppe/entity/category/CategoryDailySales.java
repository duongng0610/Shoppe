package com.e_cormerce.shoppe.entity.category;

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
        name = "category_daily_sales",
        indexes = {
                @Index(name = "idx_category_id", columnList = "category_id"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDailySales {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "view_date", nullable = false)
    LocalDate viewSale;

    @Column(name = "total_revenue", precision = 15, scale = 2, nullable = false)
    BigDecimal totalRevenue;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}

package com.e_cormerce.shoppe.entity.analytic;

import com.e_cormerce.shoppe.entity.category.Category;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "category_daily",
        indexes = {
                @Index(name = "idx_category_id", columnList = "category_id"),
                @Index(name = "idx_category_id", columnList = "category_id"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDaily {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "view_date", nullable = false)
    LocalDate viewDate;

    @Column(name = "total_searches", nullable = false, columnDefinition = "int default 0")
    Integer totalSearches;

    @Column(name = "total_revenue", precision = 15, scale = 2, nullable = false)
    BigDecimal totalRevenue;

    @Column(name = "total_orders", nullable = false, columnDefinition = "int default 0")
    Integer totalOrders;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}

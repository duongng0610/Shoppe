package com.e_cormerce.shoppe.entity.category;

import com.e_cormerce.shoppe.entity.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "category_daily_views",
        indexes = {
                @Index(name = "idx_category_id", columnList = "category_id"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDailyView {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "view_date", nullable = false)
    LocalDate viewDate;

    @Column(name = "total_view", nullable = false)
    @Positive
    BigDecimal totalViews;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}

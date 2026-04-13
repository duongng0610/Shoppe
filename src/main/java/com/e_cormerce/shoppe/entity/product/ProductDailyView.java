package com.e_cormerce.shoppe.entity.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_daily_views",
        indexes = {
                @Index(name = "idx_product", columnList = "product_id"),
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDailyView {
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
    @JoinColumn(name = "product_id")
    Product product;
}

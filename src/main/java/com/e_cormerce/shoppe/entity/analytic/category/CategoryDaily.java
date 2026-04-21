package com.e_cormerce.shoppe.entity.analytic.category;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "category_daily",
        indexes = {
                @Index(name = "idx_category_id", columnList = "category_id,date"),

        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(CategoryDailyId.class)
public class CategoryDaily {

    @Id
    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(name = "total_searches", nullable = false, columnDefinition = "int default 0")
    int totalSearches;

    @Column(name = "total_new_products", nullable = false, columnDefinition = "int default 0")
    int totalNewProducts;

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


    @Id
    @Column(name = "category_id", nullable = false)
    String categoryId;
    

}

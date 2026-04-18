package com.e_cormerce.shoppe.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(
        name = "product_review_images",
        indexes = {
                @Index(name = "idx_product_review_images_product", columnList = "product_review_id")
        })
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@SQLDelete(sql = "UPDATE product_review_images SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class ProductReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @Column(nullable = false)
    String url;

    @ManyToOne
    @JoinColumn(name = "product_review_id", nullable = false)
    ProductReview productReview;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Date updatedAt;
}

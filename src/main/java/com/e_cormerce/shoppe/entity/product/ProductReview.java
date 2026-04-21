package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
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
        name = "product_reviews",
        indexes = {
                @Index(name = "idx_product_reviews_product", columnList = "product_id"),
                @Index(name = "idx_product_reviews_client", columnList = "client_id")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE product_reviews SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @Column(nullable = false)
    int rate;

    @Column(columnDefinition = "TEXT", nullable = false)
    String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}

package com.e_cormerce.shoppe.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "product_review_images",
    indexes = {@Index(name = "idx_product_review_images_product", columnList = "product_id")})
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
  @JoinColumn(name = "product_id", nullable = false)
  Product product;
}

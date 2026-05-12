package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "products",
    indexes = {
      @Index(name = "idx_products_category", columnList = "category_id"),
      @Index(name = "idx_products_seller", columnList = "seller_id"),
      @Index(name = "idx_products_seller_status", columnList = "seller_id, status")
    })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE products SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  String thumbnail;

  @Column(nullable = false, columnDefinition = "TEXT")
  String description;

  @Column(name = "origin_price", precision = 15, scale = 2, nullable = false)
  BigDecimal originPrice;

  @Column(name = "discount_percentage", columnDefinition = "float default 0")
  Float discountPercentage;

  @Column(name = "rate", nullable = true)
  @Min(value = 1, message = "Rating must be at least 1")
  @Max(value = 5, message = "Rating must not exceed 5")
  Float rate;

  @Column(name = "total_quantity", nullable = false, columnDefinition = "int default 0")
  int totalQuantity;

  @Column(name = "total_quantity_sold", columnDefinition = "float default 0")
  int totalQuantitySold;

  @Column(name = "has_variant", nullable = false)
  boolean hasVariant;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  ProductStatus status;

  @CreationTimestamp
  @Column(
      name = "created_at",
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  Date updatedAt;

  // owner side
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  Category category;

  // inverse side
  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  List<Type> types;

  // inverse side
  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  List<Variant> variants;

  // inverse side
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  List<ProductExtraImage> productExtraImages;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  User seller;

  public void addVariant(Variant variant) {
    if (variants == null) {
      variants = new ArrayList<>();
    }
    variants.add(variant);
  }
}

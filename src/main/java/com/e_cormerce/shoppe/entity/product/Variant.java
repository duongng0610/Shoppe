package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "variants",
    indexes = {@Index(name = "idx_product", columnList = "product_id")})
@SQLDelete(sql = "UPDATE type_value SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Variant {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(precision = 15, scale = 2, nullable = false)
  BigDecimal price;

  @Column(nullable = false)
  String thumbnail;

  @Column(nullable = false, columnDefinition = "int default 0")
  int quantity;

  @Column(nullable = false, columnDefinition = "int default 0")
  int reserved;

  @Column(name = "quantity_sold", columnDefinition = "int default 0")
  int quantitySold;

  @Column(name = "discount_percentage", columnDefinition = "float default 0")
  Float discountPercentage;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(name = "is_default", columnDefinition = "boolean default false")
  boolean isDefault;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  @JsonIgnore
  Product product;

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  Set<VariantValue> variantValues;

  @CreationTimestamp
  @Column(
      name = "created_at",
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  Date updatedAt;
}

package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

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
public class Variant {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(precision = 15, scale = 2, nullable = false)
  BigDecimal price;

  @Column(nullable = false)
  String thumbnail;

  @Column(nullable = false)
  int quantity;

  @Column(name = "quantity_sold")
  int quantitySold;

  @Column(name = "discount_percentage")
  double discountPercentage;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(name = "is_default", columnDefinition = "boolean default true")
  boolean isDefault;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  @JsonIgnore
  Product product;

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Nullable
  List<VariantValue> variantValues;
}

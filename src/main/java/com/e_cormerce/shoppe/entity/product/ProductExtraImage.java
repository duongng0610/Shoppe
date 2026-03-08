package com.e_cormerce.shoppe.entity.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "product_extra_images",
    indexes = {@Index(name = "idx_product", columnList = "product_id")})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductExtraImage {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String url;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  Product product;
}

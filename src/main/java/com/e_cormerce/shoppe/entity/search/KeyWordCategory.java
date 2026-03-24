package com.e_cormerce.shoppe.entity.search;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "keyword_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyWordCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String keyword;
  String category_id;

  @DecimalMin(value = "0.0", inclusive = false)
  @DecimalMax(value = "1.0", inclusive = true)
  Float weight;
}

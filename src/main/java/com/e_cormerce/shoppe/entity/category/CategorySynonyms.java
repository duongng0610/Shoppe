package com.e_cormerce.shoppe.entity.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "category_synonyms",
    indexes = {@Index(name = "idx_category", columnList = "category_id")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE category_synonyms SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class CategorySynonyms {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  Category category;

  @Column(nullable = false, unique = true)
  String val;

  @DecimalMin(value = "0.0", inclusive = false)
  @DecimalMax(value = "1.0", inclusive = true)
  Float weight;
}

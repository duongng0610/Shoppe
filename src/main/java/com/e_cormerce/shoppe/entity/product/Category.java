package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "categories",
    indexes = {
      @Index(name = "idx_parent", columnList = "parent_id"),
      @Index(name = "idx_val", columnList = "val")
    })
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String val;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  String thumbnail;

  // owner side
  @ManyToOne
  @JoinColumn(name = "parent_id")
  @Nullable
  @JsonIgnore
  Category parent;
}

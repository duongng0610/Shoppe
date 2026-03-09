package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;
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
  @JsonIgnore
  Category parent;

  // inverse side
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  Set<Category> children;

  // inverse side
  @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
  @JsonIgnore
  Set<Product> products;
}

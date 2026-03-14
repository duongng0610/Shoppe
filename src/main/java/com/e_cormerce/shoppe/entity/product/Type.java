package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Type {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String val;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  // owner side
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  @JsonIgnore
  Product product;

  // inverse side
  @OneToMany(
      mappedBy = "type",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  List<TypeValue> typeValues;
}

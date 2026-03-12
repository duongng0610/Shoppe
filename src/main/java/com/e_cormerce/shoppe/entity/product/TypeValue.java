package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeValue {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String val;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  // owner side
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "type_id")
  @JsonIgnore
  Type type;
}

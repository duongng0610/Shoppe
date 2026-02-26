package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "variant_values",
        indexes = {@Index(name = "idx_variant", columnList = "variant_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // owner side
    @ManyToOne
    @JoinColumn(name = "variant_id")
    Variant variant;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    TypeValue value;
}

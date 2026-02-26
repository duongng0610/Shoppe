package com.e_cormerce.shoppe.entity;

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
    String val;

    // owner side
    @ManyToOne
    @JoinColumn(name = "type_id")
    Type type;


}

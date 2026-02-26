package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String val;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles;
}

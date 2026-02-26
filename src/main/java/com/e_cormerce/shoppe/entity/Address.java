package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String province;
    String ward;
    boolean is_default;

    // inverse side
    @ManyToMany(mappedBy = "addresses")
    Set<User> users;
}

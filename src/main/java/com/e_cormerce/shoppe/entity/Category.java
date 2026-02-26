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

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;

    // owner side
    @ManyToOne
    @JoinColumn(name = "parent_id")
    Category parent;

    // inverse side
    @OneToMany(mappedBy = "parent",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    Set<Category> children;

    // inverse side
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    Set<Product> products;
}

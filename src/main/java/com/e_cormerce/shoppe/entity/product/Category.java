package com.e_cormerce.shoppe.entity.product;

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
@Table(name = "categories", indexes = {@Index(name = "idx_parent", columnList = "parent_id"),
        @Index(name = "idx_val", columnList = "val")
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

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
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    Set<Product> products;
}

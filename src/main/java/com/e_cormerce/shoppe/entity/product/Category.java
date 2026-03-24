package com.e_cormerce.shoppe.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

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
                @Index(name = "idx_categories_parent", columnList = "parent_id"),
                @Index(name = "idx_categories_val", columnList = "val")
        })
@SQLDelete(sql = "UPDATE categories SET deleted=true where id=?")
@Where(
        clause = "deleted = false"
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String val;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    String thumbnail;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    // owner side
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Nullable
    @JsonIgnore
    Category parent;
}

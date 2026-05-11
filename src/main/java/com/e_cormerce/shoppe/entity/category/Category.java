package com.e_cormerce.shoppe.entity.category;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

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
      @Index(name = "idx_categories_path_to_parent", columnList = "path_to_parent"),
      @Index(name = "idx_categories_val", columnList = "val")
    })
@SQLDelete(sql = "UPDATE categories SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false)
  String val;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  String thumbnail;

  @Column(
      name = "created_at",
      nullable = false,
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @CreationTimestamp
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @Column(name = "path_to_parent")
  String pathToParent;
}

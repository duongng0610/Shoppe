package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    name = "shopping_carts",
    indexes = {@Index(name = "idx_shopping_carts_client", columnList = "client_id")})
@SQLDelete(sql = "UPDATE shopping_carts SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class ShoppingCart {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(name = "total_quantity", nullable = false)
  int totalQuantity;

  @Column(name = "name", nullable = false, columnDefinition = "varchar(255) default 'Mặc định'")
  String name;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shoppingCart")
  List<ShoppingCartItem> shoppingCartItems;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  User client;

  @CreationTimestamp
  @Column(
      name = "created_at",
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  Date updatedAt;
}

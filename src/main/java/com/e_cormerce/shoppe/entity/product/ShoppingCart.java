package com.e_cormerce.shoppe.entity.product;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

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

    @Column(name = "price_each", precision = 15, scale = 2, nullable = false)
    BigDecimal totalPrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shoppingCart")
    List<ShoppingCartItem> shoppingCartItems;


    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    User client;
}

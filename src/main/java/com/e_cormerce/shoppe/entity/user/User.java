package com.e_cormerce.shoppe.entity.user;

import com.e_cormerce.shoppe.entity.Role;
import com.e_cormerce.shoppe.entity.transaction.Transaction;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users",
        indexes = {@Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_role", columnList = "role")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true)
    String username;

    String avatar;
    LocalDate birth;
    LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserStatus user_status;

    // owner side
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    Account account;

    // owner side
    @ManyToMany
    @JoinTable(
            name = "user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"),
            indexes = {@Index(name = "idx_user", columnList = "user_id")}
    )
    Set<Address> addresses;

    // owner side
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    // inverse side
    @OneToMany(mappedBy = "client", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Order> orders;

    // inverse side
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ShoppingCartItem> shoppingCartItems;

    // inverse side
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PhoneNumber> phoneNumbers;

    //inverse side
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Transaction> transactions;

}

package com.e_cormerce.shoppe.entity;

import com.e_cormerce.shoppe.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
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
    String username;
    String avatar;
    Date birth;
    Date created_at;
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
    @JoinColumn(name = "role_id")
    Role role;

    // inverse side
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Order> orders;

    // inverse side
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ShoppingCartItem> shoppingCartItems;

    // inverse side
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PhoneNumber> phoneNumbers;


}

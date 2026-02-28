package com.e_cormerce.shoppe.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(
        name = "accounts",
        indexes = {@Index(name = "idx_email", columnList = "email")}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(name = "created_at", nullable = false)
    @NotNull
    Date created_at;

    Date updated_at;

    @NotNull
    Date last_login_at;

    @NotNull
    Date last_active_at;
    @NotNull
    boolean is_active;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    // inverse side
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    User user;
}

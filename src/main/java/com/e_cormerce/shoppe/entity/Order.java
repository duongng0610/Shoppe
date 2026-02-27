package com.e_cormerce.shoppe.entity;

import com.e_cormerce.shoppe.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders", indexes = {@Index(name = "idx_user", columnList = "user_id"),
        @Index(name = "idx_product", columnList = "variant_id")
})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    OrderStatus status;
    @Column(name = "created_at", nullable = false)
    @NotNull
    Date created_at;
    Date updated_at;
    double price_each;
    int quantity;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    Variant variant;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

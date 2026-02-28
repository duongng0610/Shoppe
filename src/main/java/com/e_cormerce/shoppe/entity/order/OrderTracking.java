package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal ship_cost;

    @Column(nullable = false)
    LocalDate required_date;

    @Column(nullable = false)
    LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "shipper_id", nullable = false)
    User shipper;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    
}

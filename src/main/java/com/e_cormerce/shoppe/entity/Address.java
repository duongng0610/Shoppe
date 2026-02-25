package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String province;
    String ward;
    String address_detail;
    boolean is_default;

    @ManyToOne
    @JoinColumn(name = "client_id")
    User client;

}

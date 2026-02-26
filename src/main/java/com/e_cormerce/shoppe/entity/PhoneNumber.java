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
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String val;
    String is_default;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}

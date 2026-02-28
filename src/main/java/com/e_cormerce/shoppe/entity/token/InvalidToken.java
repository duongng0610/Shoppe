package com.e_cormerce.shoppe.entity.token;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "invalid_tokens", indexes = {@Index(name = "idx_token", columnList = "val")})
public class InvalidToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true)
    String val;

    @Column(nullable = false)
    LocalDate invalid_date;


}

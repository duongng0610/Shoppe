package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
    String val;
    Date invalid_date;


}

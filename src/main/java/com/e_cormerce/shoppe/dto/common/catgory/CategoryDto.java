package com.e_cormerce.shoppe.dto.common.catgory;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    String id;
    String val;
    LocalDateTime createdAt;
}

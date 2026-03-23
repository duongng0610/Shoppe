package com.e_cormerce.shoppe.dto.common.search;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CategoryProjection {
    String name;
    String categoryId;
}

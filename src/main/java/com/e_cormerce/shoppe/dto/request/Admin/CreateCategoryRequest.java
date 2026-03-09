package com.e_cormerce.shoppe.dto.request.Admin;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CreateCategoryRequest {
    String name;
    String parent_id;
}

package com.e_cormerce.shoppe.dto.response.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDetailResponse {
    String id;
    String val;
    String thumbnail;
    boolean deleted;
    Date createdAt;
    String parentVal;
}

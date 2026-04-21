package com.e_cormerce.shoppe.event.catgory;


import com.e_cormerce.shoppe.dto.common.catgory.CategoryDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySearched {
    CategoryDto category;
    LocalDateTime createdAt;
}

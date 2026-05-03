package com.e_cormerce.shoppe.event.catgory;

import com.e_cormerce.shoppe.dto.common.catgory.CategoryDto;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySearched {
  CategoryDto category;
  LocalDateTime createdAt;
}

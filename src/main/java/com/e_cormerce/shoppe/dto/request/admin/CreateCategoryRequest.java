package com.e_cormerce.shoppe.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryRequest {

  @NotBlank(message = "Name category is required")
  String name;

  String parentId;
  List<String> synonyms;
  String thumbnailId;
}

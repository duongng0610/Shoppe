package com.e_cormerce.shoppe.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryRequest {
    MultipartFile image;
    @NotBlank(message = "Name must not be blank")
    String name;
    @NotBlank(message = "Name must not be blank")
    String parentId;
}

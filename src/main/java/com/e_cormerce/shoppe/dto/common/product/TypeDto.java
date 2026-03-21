package com.e_cormerce.shoppe.dto.common.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Valid
public class TypeDto {
    @NotBlank(message = "type name is required")
    String name;

    @NotNull(message = "type values is required")
    @Size(min = 1, message = "type values must not be empty")
    List<String> values;
}

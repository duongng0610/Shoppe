package com.e_cormerce.shoppe.dto.common;

import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeResponse {
  String name;
  List<TypeValueResponse> typeValues;
}

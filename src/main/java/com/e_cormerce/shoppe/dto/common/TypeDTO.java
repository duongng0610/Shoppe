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
public class TypeDTO {
  String typeName;
  List<String> typeValues;
}

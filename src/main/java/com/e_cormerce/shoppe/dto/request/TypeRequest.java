package com.e_cormerce.shoppe.dto.request;

import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeRequest {
  String typeName;
  List<String> typeValues;
}

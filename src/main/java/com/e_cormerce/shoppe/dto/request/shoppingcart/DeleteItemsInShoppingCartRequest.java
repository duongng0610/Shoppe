package com.e_cormerce.shoppe.dto.request.shoppingcart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteItemsInShoppingCartRequest {
  @NotEmpty(message = "list of delete shopping cart item is not null")
  List<@NotBlank(message = "can not delete null shopping cart item") String> itemIds;
}

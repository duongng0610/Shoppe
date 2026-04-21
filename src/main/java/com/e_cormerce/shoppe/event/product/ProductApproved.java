package com.e_cormerce.shoppe.event.product;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductApproved {
    ProductCardResponse product;
    UserDto seller;

}
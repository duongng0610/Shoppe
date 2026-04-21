package com.e_cormerce.shoppe.event.product;

import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStatusUpdated {
    ProductStatus oldStatus;
    Product product;
    User seller;
}

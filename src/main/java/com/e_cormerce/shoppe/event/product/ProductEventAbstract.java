package com.e_cormerce.shoppe.event.product;

import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ProductEventAbstract {
    Product product;
    User seller;
    Category category;

    
}

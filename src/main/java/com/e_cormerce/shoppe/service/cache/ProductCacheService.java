package com.e_cormerce.shoppe.service.cache;

import com.e_cormerce.shoppe.dto.response.product.MyProductResponse;
import com.e_cormerce.shoppe.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCacheService {
    ProductService productService;

    @Cacheable(value = "products", key = "#id", sync = true)
    public MyProductResponse cachingGetProductDetails(String id) {
        return productService.cleanGetProductDetails(id);
    }
}

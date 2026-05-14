package com.e_cormerce.shoppe.service.product;

import com.e_cormerce.shoppe.projection.product.ProductAnalysisProjection;
import com.e_cormerce.shoppe.projection.product.ProductDailyRecentProjection;
import com.e_cormerce.shoppe.projection.product.ProductGrowthAnalysisProjection;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductAnalysisService {
    ProductRepository productRepository;

    // public
    public List<ProductAnalysisProjection> getAllProductsForAdmin(int limit, int offset) {
        return productRepository.getAllProductsForAdmin(limit, offset);
    }


    public List<ProductDailyRecentProjection> getProductDailyRecent(String productId, Integer days) {

        return productRepository.getProductDailyRecent(productId, days);
    }

    public ProductGrowthAnalysisProjection analyzeProductGrowth(String productId, Integer days) {

        return productRepository.analyzeProductGrowth(productId, days);
    }
}

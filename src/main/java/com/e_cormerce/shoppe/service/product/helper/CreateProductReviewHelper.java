package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.request.product.CreateProductReviewRequest;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.ProductReview;
import com.e_cormerce.shoppe.entity.product.ProductReviewImage;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateProductReviewHelper {
    ProductRepository productRepository;

    public void createProductReviewImage(CreateProductReviewRequest request, ProductReview productReview) {
        List<ProductReviewImage> images = request.getImages().stream()
                .map(imageUrl -> {
                    ProductReviewImage image = ProductReviewImage.builder()
                            .url(imageUrl)
                            .productReview(productReview)
                            .build();

                    return image;
                }).toList();

        productReview.setImages(images);
    }

    public Product getProduct(String orderId) {
        return productRepository.findProductOfOrder(orderId);
    }
}

package com.e_cormerce.shoppe.service.admin;

import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminService {
    ProductRepository productRepository;

    @Transactional
    public void approveProducts(List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_APPROVE_PRODUCT_REQUEST);
        }
        productRepository.updateStatus(productIds, ProductStatus.APPROVED.getValue());
    }
}

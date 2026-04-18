package com.e_cormerce.shoppe.service.order.helper;

import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class CreateOrderHelper {
    VariantRepository variantRepository;
    UserRepository userRepository;
    AuthService authService;

    public Variant getVariant(@NotBlank String variantId) {
        return variantRepository
                .findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_VARIANT));
    }

    public User getSeller(@NotBlank String sellerId) {
        return userRepository
                .findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
    }

    public User getClient() {
        return authService.getUserThroughAuthentication();
    }


    private String getProductName(Variant variant) {
        return variantRepository.getProductName(variant.getId());
    }

    private String getVariantName(Variant variant) {
        List<String> variantValues = variantRepository.getVariantValues(variant.getId());

        String variantName = variantValues.stream().collect(Collectors.joining(", "));

        return variantName;
    }

    public BigDecimal getTotalPrice(Variant variant, int quantity) {
        BigDecimal totalPrice = variant.getPrice().multiply(BigDecimal.valueOf(quantity));
        return totalPrice;
    }

    public void getOrderInfo(Order order, Variant variant) {
        order.setProductName(getProductName(variant));
        order.setVariantName(getVariantName(variant));
    }

    public String getUserId() {
        return authService.getUserId();
    }
}

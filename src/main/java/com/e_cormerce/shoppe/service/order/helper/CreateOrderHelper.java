package com.e_cormerce.shoppe.service.order.helper;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class CreateOrderHelper {
    VariantRepository variantRepository;
    UserRepository userRepository;
    AuthService authService;
    ObjectMapper objectMapper;

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


    public BigDecimal getTotalPrice(Variant variant, int quantity) {
        BigDecimal totalPrice = variant.getPrice().multiply(BigDecimal.valueOf(quantity));
        return totalPrice;
    }

    public String getAttributesVariant(Variant variant) {
        try {
            return objectMapper.writeValueAsString(
                    variant.getVariantValues().stream().map(variantValue ->
                            VariantAttributeDto.builder().name(variantValue.getValue().getType().getVal()).value(variantValue.getValue().getVal()).build()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkTotalPrice(Variant variant, BigDecimal totalPrice) {

    }


    public String getUserId() {
        return authService.getUserId();
    }
}

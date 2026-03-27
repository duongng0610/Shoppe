package com.e_cormerce.shoppe.service.shoppingcart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.product.ShoppingCartItemRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartHelper {
    VariantRepository variantRepository;
    ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCartItem createShoppingCartItem(AddItemToShoppingCartRequest request) {
        Variant variant =
                variantRepository
                        .findById(request.getVariantId())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_VARIANT));

        ShoppingCartItem item =
                ShoppingCartItem.builder()
                        .isVariant(!variant.isDefault())
                        .quantity(request.getQuantity())
                        .variant(variant)
                        .priceEach(variant.getPrice())
                        .build();

        shoppingCartItemRepository.save(item);

        return item;
    }

    public BigDecimal deleteShoppingCartItems(List<String> itemIds) {
        List<ShoppingCartItem> items = shoppingCartItemRepository.findByIdIn(itemIds);
        BigDecimal itemsTotal =
                items.stream()
                        .map(item -> item.getPriceEach().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        shoppingCartItemRepository.deleteAll(items);

        return itemsTotal;
    }
}

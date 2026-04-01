package com.e_cormerce.shoppe.service.shoppingcart.helper;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.repository.shopping_cart.ShoppingCartItemRepository;
import com.e_cormerce.shoppe.repository.shopping_cart.ShoppingCartRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartHelper {
  VariantRepository variantRepository;
  ShoppingCartItemRepository shoppingCartItemRepository;
  AuthService authService;
  ShoppingCartRepository shoppingCartRepository;

  public ShoppingCartItem createShoppingCartItem(
      AddItemToShoppingCartRequest request, ShoppingCart shoppingCart) {
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
            .shoppingCart(shoppingCart)
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

  public ShoppingCart getCardOfUser() {
    User user = authService.getUserThroughAuthentication();
    ShoppingCart shoppingCart =
        shoppingCartRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_SHOPPING_CART));

    return shoppingCart;
  }
}

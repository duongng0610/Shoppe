package com.e_cormerce.shoppe.service.shoppingcart;


import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartCountResponse;
import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartItemDto;
import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.shopping_cart.ShoppingCartItemMapper;
import com.e_cormerce.shoppe.repository.product.ShoppingCartRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartService {
    ShoppingCartRepository shoppingCartRepository;
    ShoppingCartHelper shoppingCartHelper;
    ShoppingCartItemMapper shoppingCartItemMapper;
    VariantRepository variantRepository;

    private ShoppingCart getCartByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return shoppingCartRepository.findByUserId(authentication.getPrincipal().toString()).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_SHOPPING_CART));
    }

    @Transactional
    public void addItem(AddItemToShoppingCartRequest request) {

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

        item.setShoppingCart(getCartByUser());
        
        BigDecimal itemTotal = item.getPriceEach().multiply(BigDecimal.valueOf(item.getQuantity()));

        BigDecimal totalPrice = getCartByUser().getTotalPrice().add(itemTotal);

        getCartByUser().setTotalPrice(totalPrice);
        getCartByUser().setTotalQuantity(getCartByUser().getTotalQuantity() + 1);

        shoppingCartRepository.save(getCartByUser());
    }

    @Transactional
    public void deleteItem(DeleteItemsInShoppingCartRequest request) {

        BigDecimal itemTotal = shoppingCartHelper.deleteShoppingCartItems(request.getItemIds());
        BigDecimal totalPrice = getCartByUser().getTotalPrice().subtract(itemTotal);

        getCartByUser().setTotalPrice(totalPrice);
        getCartByUser().setTotalQuantity(getCartByUser().getTotalQuantity() - 1);
    }

    public ShoppingCartCountResponse getBasicShoppingCart() {
        return ShoppingCartCountResponse.builder().count(getCartByUser().getTotalQuantity()).id(getCartByUser().getId()).build();
    }

    public List<ShoppingCartItemDto> getShoppingCartItems() {
        return getCartByUser().getShoppingCartItems().stream().map(item -> shoppingCartItemMapper.toDto(item)).toList();
    }


}

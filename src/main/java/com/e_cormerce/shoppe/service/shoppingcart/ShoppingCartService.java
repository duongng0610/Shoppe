package com.e_cormerce.shoppe.service.shoppingcart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartCountResponse;
import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartItemDto;
import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.shopping_cart.ShoppingCartItemMapper;
import com.e_cormerce.shoppe.repository.shopping_cart.ShoppingCartItemRepository;
import com.e_cormerce.shoppe.repository.shopping_cart.ShoppingCartRepository;
import com.e_cormerce.shoppe.service.shoppingcart.helper.ShoppingCartHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    ShoppingCartItemRepository shoppingCartItemRepository;

    @Transactional
    public void addItem(AddItemToShoppingCartRequest request) {
        ShoppingCart shoppingCart = shoppingCartHelper.getCardOfUser();
        ShoppingCartItem item = shoppingCartHelper.createShoppingCartItem(request, shoppingCart);

        BigDecimal itemTotal = item.getPriceEach().multiply(BigDecimal.valueOf(item.getQuantity()));

        shoppingCart.setTotalQuantity(shoppingCart.getTotalQuantity() + 1);

        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    public void deleteItems(DeleteItemsInShoppingCartRequest request) {

        ShoppingCart shoppingCart = shoppingCartHelper.getCardOfUser();
        BigDecimal itemTotal = shoppingCartHelper.deleteShoppingCartItems(request.getItemIds());

        shoppingCart.setTotalQuantity(shoppingCart.getTotalQuantity() - request.getItemIds().size());
    }

    public ShoppingCartCountResponse getBasicShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartHelper.getCardOfUser();
        return ShoppingCartCountResponse.builder()
                .count(shoppingCart.getTotalQuantity())
                .id(shoppingCart.getId())
                .build();
    }

    public List<ShoppingCartItemDto> getShoppingCartItems() {
        ShoppingCart shoppingCart = shoppingCartHelper.getCardOfUser();
        return shoppingCart.getShoppingCartItems().stream()
                .map(
                        item -> {
                            var resItem = shoppingCartItemMapper.toDto(item);
                            resItem.setProduct(
                                    shoppingCartItemRepository
                                            .findProductByCartItemId(item.getId().toString())
                                            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT)));
                            return resItem;
                        })
                .toList();
    }
}

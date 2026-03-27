package com.e_cormerce.shoppe.service.shoppingcart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartCountResponse;
import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartItemDto;
import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.mapper.shopping_cart.ShoppingCartItemMapper;
import com.e_cormerce.shoppe.repository.product.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartService {
  ShoppingCartRepository shoppingCartRepository;
  ShoppingCartHelper shoppingCartHelper;
  ShoppingCartItemMapper shoppingCartItemMapper;

  @Transactional
  public void addItem(AddItemToShoppingCartRequest request) {
    ShoppingCart shoppingCart = shoppingCartHelper.getCardOfUser();
    ShoppingCartItem item = shoppingCartHelper.createShoppingCartItem(request, shoppingCart);

    BigDecimal itemTotal = item.getPriceEach().multiply(BigDecimal.valueOf(item.getQuantity()));
    BigDecimal totalPrice = shoppingCart.getTotalPrice().add(itemTotal);

    shoppingCart.setTotalPrice(totalPrice);
    shoppingCart.setTotalQuantity(shoppingCart.getTotalQuantity() + 1);

    shoppingCartRepository.save(shoppingCart);
  }

  @Transactional
  public void deleteItem(DeleteItemsInShoppingCartRequest request) {
    ShoppingCart shoppingCart = shoppingCartHelper.getCardOfUser();

    BigDecimal itemTotal = shoppingCartHelper.deleteShoppingCartItems(request.getItemIds());
    BigDecimal totalPrice = shoppingCart.getTotalPrice().subtract(itemTotal);

    shoppingCart.setTotalPrice(totalPrice);
    shoppingCart.setTotalQuantity(shoppingCart.getTotalQuantity() - 1);
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
        .map(item -> shoppingCartItemMapper.toDto(item))
        .toList();
  }
}

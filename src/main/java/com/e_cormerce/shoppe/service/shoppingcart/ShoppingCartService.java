package com.e_cormerce.shoppe.service.shoppingcart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.product.ShoppingCartRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartService {
  ShoppingCartRepository shoppingCartRepository;
  ShoppingCartHelper shoppingCartHelper;
  UserRepository userRepository;

  @Transactional
  public void addItem(AddItemToShoppingCartRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));

    ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());

    ShoppingCartItem item = shoppingCartHelper.createShoppingCartItem(request);
    item.setShoppingCart(shoppingCart);

    BigDecimal itemTotal = item.getPriceEach().multiply(BigDecimal.valueOf(item.getQuantity()));

    BigDecimal totalPrice = shoppingCart.getTotalPrice().add(itemTotal);

    shoppingCart.setTotalPrice(totalPrice);
    shoppingCart.setTotalQuantity(shoppingCart.getTotalQuantity() + 1);

    shoppingCartRepository.save(shoppingCart);
  }

  @Transactional
  public void deleteItem(DeleteItemsInShoppingCartRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));

    ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());

    BigDecimal itemTotal = shoppingCartHelper.deleteShoppingCartItems(request.getItemIds());
    BigDecimal totalPrice = shoppingCart.getTotalPrice().subtract(itemTotal);

    shoppingCart.setTotalPrice(totalPrice);
    shoppingCart.setTotalQuantity(shoppingCart.getTotalQuantity() - 1);
  }
}

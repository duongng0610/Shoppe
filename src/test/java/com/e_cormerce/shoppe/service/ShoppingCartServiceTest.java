// package com.e_cormerce.shoppe.service;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.Mockito.when;
//
// import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
// import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
// import com.e_cormerce.shoppe.entity.product.ShoppingCart;
// import com.e_cormerce.shoppe.entity.product.ShoppingCartItem;
// import com.e_cormerce.shoppe.entity.user.User;
// import com.e_cormerce.shoppe.enums.ErrorCode;
// import com.e_cormerce.shoppe.exception.AppException;
// import com.e_cormerce.shoppe.repository.shopping_cart.ShoppingCartRepository;
// import com.e_cormerce.shoppe.repository.user.UserRepository;
// import com.e_cormerce.shoppe.service.shoppingcart.ShoppingCartService;
// import com.e_cormerce.shoppe.service.shoppingcart.helper.ShoppingCartHelper;
// import java.math.BigDecimal;
// import java.util.List;
// import java.util.Optional;
// import lombok.AccessLevel;
// import lombok.experimental.FieldDefaults;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;
//
// @ExtendWith(MockitoExtension.class)
// @FieldDefaults(level = AccessLevel.PRIVATE)
// public class ShoppingCartServiceTest {
//
//  @Mock ShoppingCartRepository shoppingCartRepository;
//
//  @Mock ShoppingCartHelper shoppingCartHelper;
//
//  @Mock UserRepository userRepository;
//
//  @InjectMocks ShoppingCartService shoppingCartService;
//
//  private void mockAuthentication(String username) {
//    Authentication authentication = Mockito.mock(Authentication.class);
//    when(authentication.getName()).thenReturn(username);
//
//    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//    when(securityContext.getAuthentication()).thenReturn(authentication);
//
//    SecurityContextHolder.setContext(securityContext);
//  }
//
//  @Test
//  void testAddItem_success() {
//    String username = "user1";
//    mockAuthentication(username);
//
//    User user = new User();
//    user.setId("u1");
//
//    ShoppingCart cart = new ShoppingCart();
//    cart.setId("c1");
//    cart.setTotalPrice(BigDecimal.valueOf(100));
//    cart.setTotalQuantity(1);
//
//    ShoppingCartItem item = new ShoppingCartItem();
//    item.setPriceEach(BigDecimal.valueOf(50));
//    item.setQuantity(2);
//
//    AddItemToShoppingCartRequest request = new AddItemToShoppingCartRequest("v1", 2);
//
//    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//
//    when(shoppingCartRepository
//            .findByUserId("u1")
//            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_SHOPPING_CART)))
//        .thenReturn(cart);
//
//    when(shoppingCartHelper.createShoppingCartItem(request)).thenReturn(item);
//
//    shoppingCartService.addItem(request);
//
//    assertEquals(BigDecimal.valueOf(200), cart.getTotalPrice());
//    assertEquals(2, cart.getTotalQuantity());
//
//    Mockito.verify(shoppingCartRepository).save(cart);
//  }
//
//  @Test
//  void testAddItem_userNotFound() {
//    String username = "user1";
//    mockAuthentication(username);
//
//    AddItemToShoppingCartRequest request = new AddItemToShoppingCartRequest("v1", 2);
//
//    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//    assertThrows(AppException.class, () -> shoppingCartService.addItem(request));
//  }
//
//  @Test
//  void testDeleteItem_success() {
//    String username = "user1";
//    mockAuthentication(username);
//
//    User user = new User();
//    user.setId("u1");
//
//    ShoppingCart cart = new ShoppingCart();
//    cart.setTotalPrice(BigDecimal.valueOf(200));
//    cart.setTotalQuantity(2);
//
//    DeleteItemsInShoppingCartRequest request =
//        new DeleteItemsInShoppingCartRequest(List.of("i1", "i2"));
//
//    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//
//    when(shoppingCartRepository
//            .findByUserId("u1")
//            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_SHOPPING_CART)))
//        .thenReturn(cart);
//
//    when(shoppingCartHelper.deleteShoppingCartItems(request.getItemIds()))
//        .thenReturn(BigDecimal.valueOf(100));
//
//    shoppingCartService.deleteItems(request);
//
//    assertEquals(BigDecimal.valueOf(100), cart.getTotalPrice());
//    assertEquals(1, cart.getTotalQuantity());
//  }
// }

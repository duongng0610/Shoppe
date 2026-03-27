package com.e_cormerce.shoppe.controller.shoppingcart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.shoppingcart.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping_cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartController {
  ShoppingCartService shoppingCartService;

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('PERMISSION_ADD_CART_SHOPPING')")
  public ResponseEntity<ApiResponse> addItem(
      @RequestBody @Valid AddItemToShoppingCartRequest request) {
    shoppingCartService.addItem(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.builder()
                .message("add item to shopping cart successfully")
                .success(true)
                .build());
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('PERMISSION_DELETE_CART_SHOPPING')")
  public ResponseEntity<ApiResponse> deleteItem(
      @RequestBody @Valid DeleteItemsInShoppingCartRequest request) {
    shoppingCartService.deleteItem(request);

    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .message("delete list of item to shopping cart successfully")
                .success(true)
                .build());
  }
}

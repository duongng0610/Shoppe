package com.e_cormerce.shoppe.client.shoppingcart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartCountResponse;
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
@RequestMapping("client/shopping-cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingCartController {
    ShoppingCartService shoppingCartService;

    @GetMapping()
    //@PreAuthorize
    public ResponseEntity<ApiResponse> getAllShoppingCartItems() {
        return ResponseEntity.ok()
                .body(
                        ApiResponse.builder()
                                .data(shoppingCartService.getShoppingCartItems())
                                .message("get shopping cart count successfully")
                                .success(true)
                                .build());
    }

    @GetMapping("/count")
    //@PreAuthorize
    public ResponseEntity<ApiResponse<ShoppingCartCountResponse>> getShoppingCartCount() {
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<ShoppingCartCountResponse>builder()
                                .data(shoppingCartService.getBasicShoppingCart())
                                .message("get shopping cart count successfully")
                                .success(true)
                                .build());
    }

    @PostMapping()
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

    @DeleteMapping()
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

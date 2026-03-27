package com.e_cormerce.shoppe.controller.shopping_cart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;

public class DataTestAddItemRequestHelper {
  private DataTestAddItemRequestHelper() {}
  ;

  public static AddItemToShoppingCartRequest validRequest() {
    AddItemToShoppingCartRequest request =
        AddItemToShoppingCartRequest.builder().quantity(2).variantId("abc").build();

    return request;
  }

  public static AddItemToShoppingCartRequest requestWithNoQuantity() {
    AddItemToShoppingCartRequest request =
        AddItemToShoppingCartRequest.builder().variantId("abc").build();

    return request;
  }

  public static AddItemToShoppingCartRequest requestWithNoVariantId() {
    AddItemToShoppingCartRequest request =
        AddItemToShoppingCartRequest.builder().quantity(2).build();

    return request;
  }
}

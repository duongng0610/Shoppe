package com.e_cormerce.shoppe.controller.client.shopping_cart;

import com.e_cormerce.shoppe.dto.request.shoppingcart.DeleteItemsInShoppingCartRequest;
import java.util.ArrayList;
import java.util.List;

public class DataTestDeleteRequestHelper {
  private DataTestDeleteRequestHelper() {}
  ;

  public static DeleteItemsInShoppingCartRequest validRequest() {
    List<String> itemIds = new ArrayList<>();
    itemIds.add("abc");

    DeleteItemsInShoppingCartRequest request =
        DeleteItemsInShoppingCartRequest.builder().itemIds(itemIds).build();

    return request;
  }

  public static DeleteItemsInShoppingCartRequest requestWithNoListIds() {
    List<String> itemIds = new ArrayList<>();

    DeleteItemsInShoppingCartRequest request =
        DeleteItemsInShoppingCartRequest.builder().itemIds(itemIds).build();

    return request;
  }
}

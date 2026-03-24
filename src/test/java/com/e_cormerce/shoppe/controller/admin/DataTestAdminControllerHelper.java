package com.e_cormerce.shoppe.controller.admin;

import com.e_cormerce.shoppe.dto.request.admin.ApproveProductsRequest;
import java.util.ArrayList;
import java.util.List;

public class DataTestAdminControllerHelper {
  public static ApproveProductsRequest createListString(int size) {
    List<String> list = new ArrayList<String>();
    for (int i = 0; i < size; i++) {
      list.add(i + "");
    }
    return ApproveProductsRequest.builder().productIds(list).build();
  }
}

package com.e_cormerce.shoppe.service.search.helper;

import org.springframework.stereotype.Component;

@Component
public class SearchHelper {
  public static String toNormalize(String word) {
    return word.toLowerCase().trim().replace("//s+", " ");
  }
}

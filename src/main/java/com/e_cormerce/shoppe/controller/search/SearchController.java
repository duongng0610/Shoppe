package com.e_cormerce.shoppe.controller.search;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.service.search.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {
  SearchService searchService;

  @GetMapping("/suggest-categories")
  public ResponseEntity<ApiResponse> search(
      @RequestParam("keyword") String keyword, @RequestParam("num") int num) {
    var result = searchService.search(keyword, num);
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .data(result)
                .message("get suggest category successfully")
                .build());
  }

  @GetMapping("/suggest-products")
  public ResponseEntity<ApiResponse> searchProductsInCategories(
      @RequestParam("category_id") String category_id) {
    var result = searchService.searchProductInCategory(category_id);
    return ResponseEntity.ok()
        .body(
            ApiResponse.builder()
                .data(result)
                .message("get suggest category successfully")
                .build());
  }
}

package com.e_cormerce.shoppe.service;

import static org.mockito.Mockito.when;

import com.e_cormerce.shoppe.controller.seller.product.DataTestCreateProductRequestHelper;
import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.product.ProductService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/** Mặc dù là service nhưng do muốn test những hàm helper */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class ProductServiceTest {
  @MockitoBean ProductRepository productRepository;

  @MockitoBean AuthService authService;

  @MockitoBean CategoryRepository categoryRepository;

  /** Spy để test thật */
  @Autowired ProductService productService;

  @Test
  @WithMockUser(username = "seller")
  public void saveProduct() {
    var request = DataTestCreateProductRequestHelper.validNoVariantRequest();

    var urls = new ProductImagesUrl("thumbnail", List.of(), List.of());
    when(authService.getUserThroughAuthentication()).thenReturn(User.builder().build());
    when(categoryRepository.findById(request.getCategoryId()))
        .thenReturn(Optional.of(new Category()));

    productService.persistProduct(request);
  }
}

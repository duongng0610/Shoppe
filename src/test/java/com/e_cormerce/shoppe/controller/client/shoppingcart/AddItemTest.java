package com.e_cormerce.shoppe.controller.client.shoppingcart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.e_cormerce.shoppe.dto.request.shoppingcart.AddItemToShoppingCartRequest;
import com.e_cormerce.shoppe.service.shoppingcart.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddItemTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean ShoppingCartService shoppingCartService;

  @Autowired ObjectMapper objectMapper;

  private AddItemToShoppingCartRequest validRequest() {
    AddItemToShoppingCartRequest req = new AddItemToShoppingCartRequest();
    // set field phù hợp với DTO của bạn
    // req.setVariantId("v1");
    // req.setQuantity(1);
    return req;
  }

  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
  void addItem_success() throws Exception {

    String request =
        """
        {
            "variantId": 1,
            "quantity": 2
        }
        """;

    mockMvc
        .perform(
            post("/client/shopping-cart").contentType(MediaType.APPLICATION_JSON).content(request))
        .andExpect(status().isCreated());
  }

  // =========================
  // 2. MISSING BODY
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
  void addItem_missingBody() throws Exception {

    mockMvc.perform(post("/client/shopping-cart")).andExpect(status().isInternalServerError());

    Mockito.verify(shoppingCartService, Mockito.never()).addItem(Mockito.any());
  }

  // =========================
  // 3. INVALID BODY (VALIDATION FAIL)
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
  void addItem_invalidBody() throws Exception {

    AddItemToShoppingCartRequest req = new AddItemToShoppingCartRequest();
    // không set field required

    mockMvc
        .perform(
            post("/client/shopping-cart")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());

    Mockito.verify(shoppingCartService, Mockito.never()).addItem(Mockito.any());
  }

  // =========================
  // 4. NO AUTHORITY
  // =========================
  @Test
  @WithMockUser // không có quyền
  void addItem_noAuthority() throws Exception {

    AddItemToShoppingCartRequest req = validRequest();

    mockMvc
        .perform(
            post("/client/shopping-cart")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isForbidden());

    Mockito.verify(shoppingCartService, Mockito.never()).addItem(Mockito.any());
  }

  // =========================
  // 5. SERVICE THROW EXCEPTION
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
  void addItem_serviceThrowsException() throws Exception {

    AddItemToShoppingCartRequest req = validRequest();

    Mockito.doThrow(new RuntimeException("error")).when(shoppingCartService).addItem(Mockito.any());

    mockMvc
        .perform(
            post("/client/shopping-cart")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isInternalServerError());

    Mockito.verify(shoppingCartService).addItem(Mockito.any());
  }

  // =========================
  // 6. WRONG METHOD (GET)
  // =========================
  @Test
  void addItem_wrongMethod_get() throws Exception {

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(
                "/client/shopping-cart"))
        .andExpect(status().isOk()); // vì GET map sang API khác

    Mockito.verify(shoppingCartService, Mockito.never()).addItem(Mockito.any());
  }

  // =========================
  // 7. MULTIPLE CALL VERIFY
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
  void addItem_calledTwice() throws Exception {

    AddItemToShoppingCartRequest req = validRequest();

    mockMvc
        .perform(
            post("/client/shopping-cart")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/client/shopping-cart")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated());

    Mockito.verify(shoppingCartService, Mockito.times(2)).addItem(Mockito.any());
  }
}

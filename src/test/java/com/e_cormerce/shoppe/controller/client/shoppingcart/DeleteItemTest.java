package com.e_cormerce.shoppe.controller.client.shoppingcart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.e_cormerce.shoppe.service.shoppingcart.ShoppingCartService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PRIVATE)

class DeleteItemTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean ShoppingCartService shoppingCartService;

  String body = """
            {
              "itemIds":["1","2"]
            }
            """;

  @Test
  @WithMockUser(authorities = "PERMISSION_DELETE_CART_SHOPPING")
  void delete_success() throws Exception {
    mockMvc
        .perform(delete("/client/shopping-cart").contentType("application/json").content(body))
        .andExpect(status().isOk());

    Mockito.verify(shoppingCartService).deleteItems(Mockito.any());
  }

  @Test
  @WithMockUser
  void delete_noPermission() throws Exception {
    mockMvc
        .perform(delete("/client/shopping-cart").contentType("application/json").content(body))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_DELETE_CART_SHOPPING")
  void delete_invalidJson() throws Exception {
    mockMvc
        .perform(delete("/client/shopping-cart").contentType("application/json").content("{"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_DELETE_CART_SHOPPING")
  void delete_serviceThrow() throws Exception {
    Mockito.doThrow(new RuntimeException()).when(shoppingCartService).deleteItems(Mockito.any());

    mockMvc
        .perform(delete("/client/shopping-cart").contentType("application/json").content(body))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void delete_noAuth() throws Exception {
    mockMvc
        .perform(delete("/client/shopping-cart").contentType("application/json").content(body))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_DELETE_CART_SHOPPING")
  void delete_calledTwice() throws Exception {
    mockMvc.perform(delete("/client/shopping-cart").contentType("application/json").content(body));
    mockMvc.perform(delete("/client/shopping-cart").contentType("application/json").content(body));

    Mockito.verify(shoppingCartService, Mockito.times(2)).deleteItems(Mockito.any());
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_DELETE_CART_SHOPPING")
  void delete_emptyBody() throws Exception {
    mockMvc
        .perform(delete("/client/shopping-cart").contentType("application/json").content(""))
        .andExpect(status().isBadRequest());
  }
}

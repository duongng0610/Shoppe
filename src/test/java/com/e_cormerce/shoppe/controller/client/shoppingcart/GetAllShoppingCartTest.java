package com.e_cormerce.shoppe.controller.client.shoppingcart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.e_cormerce.shoppe.service.shoppingcart.ShoppingCartService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
class GetAllShoppingCartTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShoppingCartService shoppingCartService;

    // 1. SUCCESS
    @Test
    void getAll_success() throws Exception {
        Mockito.when(shoppingCartService.getShoppingCartItems()).thenReturn(null);

        mockMvc.perform(get("/client/shopping-cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(shoppingCartService).getShoppingCartItems();
    }

    // 2. SERVICE THROW
    @Test
    void getAll_serviceThrow() throws Exception {
        Mockito.when(shoppingCartService.getShoppingCartItems())
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/client/shopping-cart"))
                .andExpect(status().isInternalServerError());

        Mockito.verify(shoppingCartService).getShoppingCartItems();
    }

    // 3. MULTIPLE CALL
    @Test
    void getAll_calledTwice() throws Exception {
        mockMvc.perform(get("/client/shopping-cart")).andExpect(status().isOk());
        mockMvc.perform(get("/client/shopping-cart")).andExpect(status().isOk());

        Mockito.verify(shoppingCartService, Mockito.times(2))
                .getShoppingCartItems();
    }

    // 4. WRONG METHOD
    @Test
    void getAll_wrongMethod() throws Exception {
        mockMvc.perform(post("/client/shopping-cart"))
                .andExpect(status().isInternalServerError()); // do PreAuthorize ở POST
    }

    // 5. NULL RESPONSE
    @Test
    void getAll_nullData() throws Exception {
        Mockito.when(shoppingCartService.getShoppingCartItems()).thenReturn(null);

        mockMvc.perform(get("/client/shopping-cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    // 6. NO SERVICE CALL (INVALID URL)
    @Test
    void getAll_invalidUrl() throws Exception {
        mockMvc.perform(get("/client/shopping-cart/abc"))
                .andExpect(status().isNotFound());

        Mockito.verify(shoppingCartService, Mockito.never())
                .getShoppingCartItems();
    }

    // 7. HEADER TEST
    @Test
    void getAll_withHeader() throws Exception {
        mockMvc.perform(get("/client/shopping-cart")
                        .header("test", "123"))
                .andExpect(status().isOk());

        Mockito.verify(shoppingCartService).getShoppingCartItems();
    }
}
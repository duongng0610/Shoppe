package com.e_cormerce.shoppe.controller.client.shoppingcart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.e_cormerce.shoppe.dto.response.client.shopping_cart.ShoppingCartCountResponse;
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
public class GetShoppingCartCountTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShoppingCartService shoppingCartService;

    // =========================
    // 1. SUCCESS
    // =========================
    @Test
    void getShoppingCartCount_success() throws Exception {
        ShoppingCartCountResponse response = new ShoppingCartCountResponse();
        // giả sử có field count
        // response.setCount(5);

        Mockito.when(shoppingCartService.getBasicShoppingCart())
                .thenReturn(response);

        mockMvc.perform(get("/client/shopping-cart/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("get shopping cart count successfully"))
                .andExpect(jsonPath("$.data").exists());

        Mockito.verify(shoppingCartService).getBasicShoppingCart();
    }

    // =========================
    // 2. SERVICE RETURN NULL
    // =========================
    @Test
    void getShoppingCartCount_nullData() throws Exception {

        Mockito.when(shoppingCartService.getBasicShoppingCart())
                .thenReturn(null);

        mockMvc.perform(get("/client/shopping-cart/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist());

        Mockito.verify(shoppingCartService).getBasicShoppingCart();
    }

    // =========================
    // 3. SERVICE THROW EXCEPTION
    // =========================
    @Test
    void getShoppingCartCount_serviceThrowsException() throws Exception {

        Mockito.when(shoppingCartService.getBasicShoppingCart())
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(get("/client/shopping-cart/count"))
                .andExpect(status().isInternalServerError());

        Mockito.verify(shoppingCartService).getBasicShoppingCart();
    }

    // =========================
    // 4. WRONG METHOD (POST)
    // =========================
    @Test
    void getShoppingCartCount_wrongMethod_post() throws Exception {

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/client/shopping-cart/count"))
                .andExpect(status().isInternalServerError());

        Mockito.verify(shoppingCartService, Mockito.never())
                .getBasicShoppingCart();
    }

    // =========================
    // 5. WRONG URL
    // =========================
    @Test
    void getShoppingCartCount_wrongUrl() throws Exception {

        mockMvc.perform(get("/client/shopping-cart/counttt"))
                .andExpect(status().isInternalServerError());

        Mockito.verify(shoppingCartService, Mockito.never())
                .getBasicShoppingCart();
    }

    // =========================
    // 6. MULTIPLE CALL VERIFY
    // =========================
    @Test
    void getShoppingCartCount_calledTwice() throws Exception {

        Mockito.when(shoppingCartService.getBasicShoppingCart())
                .thenReturn(new ShoppingCartCountResponse());

        mockMvc.perform(get("/client/shopping-cart/count"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/client/shopping-cart/count"))
                .andExpect(status().isOk());

        Mockito.verify(shoppingCartService, Mockito.times(2))
                .getBasicShoppingCart();
    }

    // =========================
    // 7. EMPTY RESPONSE OBJECT
    // =========================
    @Test
    void getShoppingCartCount_emptyObject() throws Exception {

        ShoppingCartCountResponse response = new ShoppingCartCountResponse();

        Mockito.when(shoppingCartService.getBasicShoppingCart())
                .thenReturn(response);

        mockMvc.perform(get("/client/shopping-cart/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        Mockito.verify(shoppingCartService).getBasicShoppingCart();
    }
}
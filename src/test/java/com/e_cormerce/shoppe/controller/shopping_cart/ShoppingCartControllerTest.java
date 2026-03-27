package com.e_cormerce.shoppe.controller.shopping_cart;

import com.e_cormerce.shoppe.client.shoppingcart.ShoppingCartController;
import com.e_cormerce.shoppe.filter.AuthFilter;
import com.e_cormerce.shoppe.service.shoppingcart.ShoppingCartService;
import com.e_cormerce.shoppe.util.ConvertObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {ShoppingCartController.class},
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        },
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class ShoppingCartControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShoppingCartService shoppingCartService;

    @Test
    @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
    public void testValidAdd() throws Exception {
        var request = DataTestAddItemRequestHelper.validRequest();

        mockMvc
                .perform(
                        post("/shopping_cart/add")
                                .content(ConvertObject.toJson(request))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        Mockito.verify(shoppingCartService).addItem(Mockito.any());
    }

    @Test
    @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
    public void testInvalidAddWithNoQuantity() throws Exception {
        var request = DataTestAddItemRequestHelper.requestWithNoQuantity();

        mockMvc
                .perform(
                        post("/shopping_cart/add")
                                .content(ConvertObject.toJson(request))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "PERMISSION_ADD_CART_SHOPPING")
    public void testInvalidAddWithNoVariantId() throws Exception {
        var request = DataTestAddItemRequestHelper.requestWithNoVariantId();

        mockMvc
                .perform(
                        post("/shopping_cart/add")
                                .content(ConvertObject.toJson(request))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "PERMISSION_DELETE_CART_SHOPPING")
    public void testValiDelete() throws Exception {
        var request = DataTestDeleteRequestHelper.validRequest();

        mockMvc
                .perform(
                        delete("/shopping_cart/delete")
                                .content(ConvertObject.toJson(request))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "PERMISSION_DELETE_CART_SHOPPING")
    public void testInvalidRequestWithNoListIds() throws Exception {
        var request = DataTestDeleteRequestHelper.requestWithNoListIds();

        mockMvc
                .perform(
                        delete("/shopping_cart/delete")
                                .content(ConvertObject.toJson(request))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}

package com.e_cormerce.shoppe.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.service.admin.AdminService;
import com.e_cormerce.shoppe.util.ConvertObject;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminControllerTest {
  @Autowired MockMvc mockMvc;

  @MockitoBean AdminService adminService;

  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  public void testApproveProductsSuccess() throws Exception {
    mockMvc
        .perform(
            patch("/admin/products/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ConvertObject.toJson(DataTestAdminControllerHelper.createListString(1))))
        .andExpect(status().isOk());
  }
}

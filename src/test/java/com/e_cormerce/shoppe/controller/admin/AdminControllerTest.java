package com.e_cormerce.shoppe.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.e_cormerce.shoppe.service.admin.AdminService;
import com.e_cormerce.shoppe.service.category.CategoryService;
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
public class AdminControllerTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean AdminService adminService;

  @MockitoBean CategoryService categoryService;

  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  void approveProduct_success() throws Exception {
    String id = "123";

    mockMvc
        .perform(patch("/admin/products/{id}/approve", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("approve products successfully"));

    Mockito.verify(adminService).approveProducts(id);
  }

  // =========================
  // 2. SERVICE THROW EXCEPTION
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  void approveProduct_serviceThrowsException_returns500() throws Exception {
    String id = "123";

    Mockito.doThrow(new RuntimeException("error")).when(adminService).approveProducts(id);

    mockMvc
        .perform(patch("/admin/products/{id}/approve", id))
        .andExpect(status().isInternalServerError());

    Mockito.verify(adminService).approveProducts(id);
  }

  // =========================
  // 3. NULL ID (PATH MISSING)
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  void approveProduct_missingId_returns404() throws Exception {
    mockMvc.perform(patch("/admin/products//approve")).andExpect(status().isInternalServerError());

    Mockito.verify(adminService, Mockito.never()).approveProducts(Mockito.any());
  }

  // =========================
  // 4. EMPTY ID
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  void approveProduct_emptyId() throws Exception {
    String id = "";

    mockMvc
        .perform(patch("/admin/products/{id}/approve", id))
        .andExpect(status().isInternalServerError()); // spring thường không match path

    Mockito.verify(adminService, Mockito.never()).approveProducts(Mockito.any());
  }

  // =========================
  // 5. VERY LONG ID
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  void approveProduct_longId_success() throws Exception {
    String id = "a".repeat(100);

    mockMvc.perform(patch("/admin/products/{id}/approve", id)).andExpect(status().isOk());

    Mockito.verify(adminService).approveProducts(id);
  }

  // =========================
  // 6. MULTIPLE CALL VERIFY
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  void approveProduct_calledTwice() throws Exception {
    String id = "123";

    mockMvc.perform(patch("/admin/products/{id}/approve", id)).andExpect(status().isOk());

    mockMvc.perform(patch("/admin/products/{id}/approve", id)).andExpect(status().isOk());

    Mockito.verify(adminService, Mockito.times(2)).approveProducts(id);
  }

  // =========================
  // Test API reject product
  // 1. SUCCESS
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_success() throws Exception {
    String id = "123";

    mockMvc
        .perform(patch("/admin/products/{id}/reject", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("reject products successfully"));

    Mockito.verify(adminService).rejectProducts(id);
  }

  // =========================
  // 2. SERVICE THROW RUNTIME EXCEPTION
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_serviceThrowsRuntimeException_returns500() throws Exception {
    String id = "123";

    Mockito.doThrow(new RuntimeException("database error")).when(adminService).rejectProducts(id);

    mockMvc
        .perform(patch("/admin/products/{id}/reject", id))
        .andExpect(status().isInternalServerError());

    Mockito.verify(adminService).rejectProducts(id);
  }

  // =========================
  // 3. SERVICE THROW ILLEGAL ARGUMENT
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_serviceThrowsIllegalArgument_returns500() throws Exception {
    String id = "invalid-id";

    Mockito.doThrow(new IllegalArgumentException("invalid product id"))
        .when(adminService)
        .rejectProducts(id);

    mockMvc
        .perform(patch("/admin/products/{id}/reject", id))
        .andExpect(status().isInternalServerError());

    Mockito.verify(adminService).rejectProducts(id);
  }

  // =========================
  // 4. MISSING ID
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_missingId_returns500() throws Exception {

    mockMvc.perform(patch("/admin/products//reject")).andExpect(status().isInternalServerError());

    Mockito.verify(adminService, Mockito.never()).rejectProducts(Mockito.any());
  }

  // =========================
  // 5. EMPTY ID
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_emptyId_returns500() throws Exception {
    String id = "";

    mockMvc
        .perform(patch("/admin/products/{id}/reject", id))
        .andExpect(status().isInternalServerError());

    Mockito.verify(adminService, Mockito.never()).rejectProducts(Mockito.any());
  }

  // =========================
  // 6. VERY LONG ID
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_longId_success() throws Exception {
    String id = "a".repeat(200);

    mockMvc.perform(patch("/admin/products/{id}/reject", id)).andExpect(status().isOk());

    Mockito.verify(adminService).rejectProducts(id);
  }

  // =========================
  // 7. SPECIAL CHARACTER ID
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_specialCharacterId_success() throws Exception {
    String id = "@#$%^&*";

    mockMvc.perform(patch("/admin/products/{id}/reject", id)).andExpect(status().isOk());

    Mockito.verify(adminService).rejectProducts(id);
  }

  // =========================
  // 8. CALLED TWICE
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_REJECT_PRODUCTS")
  void rejectProduct_calledTwice() throws Exception {
    String id = "123";

    mockMvc.perform(patch("/admin/products/{id}/reject", id)).andExpect(status().isOk());

    mockMvc.perform(patch("/admin/products/{id}/reject", id)).andExpect(status().isOk());

    Mockito.verify(adminService, Mockito.times(2)).rejectProducts(id);
  }

  // =========================
  // 9. NO AUTHORITY
  // =========================
  @Test
  @WithMockUser(authorities = "PERMISSION_APPROVE_PRODUCTS")
  void rejectProduct_noAuthority_forbidden() throws Exception {
    String id = "123";

    mockMvc.perform(patch("/admin/products/{id}/reject", id)).andExpect(status().isForbidden());

    Mockito.verify(adminService, Mockito.never()).rejectProducts(Mockito.any());
  }

  // =========================
  // 10. NO USER LOGIN
  // =========================
  @Test
  void rejectProduct_unauthenticated_returnsForbidden() throws Exception {
    String id = "123";

    mockMvc.perform(patch("/admin/products/{id}/reject", id)).andExpect(status().isForbidden());

    Mockito.verify(adminService, Mockito.never()).rejectProducts(Mockito.any());
  }
}

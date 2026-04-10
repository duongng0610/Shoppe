package com.e_cormerce.shoppe.controller.user.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.service.account.AccountService;
import com.e_cormerce.shoppe.util.CreateMockMultipartFile;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountController {
  @Autowired MockMvc mockMvc;
  @Autowired AccountService accountService;

  @Test
  public void changeProfile() throws Exception {
    mockMvc
        .perform(
            multipart("/seller/product")
                .file(CreateMockMultipartFile.createMockImageFile("avatar")))
        .andExpect(status().isBadRequest());
  }
}

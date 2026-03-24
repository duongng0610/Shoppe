package com.e_cormerce.shoppe.controller.search;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.filter.AuthFilter;
import com.e_cormerce.shoppe.service.search.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = SearchController.class,
    excludeAutoConfiguration = {
      SecurityAutoConfiguration.class,
      SecurityFilterAutoConfiguration.class
    },
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class SearchControllerTest {
  @Autowired MockMvc mockMvc;

  @MockitoBean SearchService searchService;

  @Test
  public void testValidSearch() throws Exception {
    mockMvc
        .perform(
            get("/search/suggest")
                .param("keyword", "laptop")
                .param("num", "10")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("get suggest category successfully"));
  }
}

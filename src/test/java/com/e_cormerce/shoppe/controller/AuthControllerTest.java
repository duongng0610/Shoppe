package com.e_cormerce.shoppe.controller;

import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.util.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ===Cách 1: load full context.
 *
 * @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
 * @AutoConfigureMockMvc//bắt buộc thêm nếu dùng SpringBootTest + MockMvc để testController
 * run mock server: fake http and can catch controller endpoint.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//bắt buộc thêm nếu dùng SpringBootTest + MockMvc để testController
/**
 * Cách 2:nhẹ hơn, tập trung vào class controller duy nhất
 * nếu dùng WebMvcTest cần thêm thuộc tính addFilters = false để bỏ qua tầng filter.
 * @WebMvcTest(controllers = AuthController.class)
 * @AutoConfigureMockMvc(addFilters = false)
 */
//=====common=====
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    CookieTokenProperties cookieTokenProperties;

    @MockitoBean
    CookieUtil cookieUtil;

    @Test
    public void testLoginRequestInvalid() throws Exception {
        var request = LogInRequest.builder().email("aaaa").password("aaaa").build();

        mockMvc.perform(post("/auth/login")
                        .content(toJson(request))

                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginRequestValid() throws Exception {
        var request = LogInRequest.builder().email("van@gmail.com").password("Vant1@abc").build();

        mockMvc.perform(post("/auth/login")
                        .content(toJson(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }


    private String toJson(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}

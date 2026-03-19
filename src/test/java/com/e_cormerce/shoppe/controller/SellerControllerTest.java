package com.e_cormerce.shoppe.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerControllerTest {

}

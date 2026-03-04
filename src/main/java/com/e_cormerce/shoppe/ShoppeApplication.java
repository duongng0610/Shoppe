package com.e_cormerce.shoppe;

import com.e_cormerce.shoppe.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppeApplication.class, args);
	}

}

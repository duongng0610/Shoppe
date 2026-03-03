package com.e_cormerce.shoppe.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "dhwxpgxzl");
        config.put("api_key", "683161372337227");
        config.put("api_secret", "eKL1dtmtWHG6NwYiw52-Ubnt4-U");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}

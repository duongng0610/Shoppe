package com.e_cormerce.shoppe.util;

import org.springframework.beans.factory.annotation.Value;


public class Constants {
    @Value("${cloudinary.cloud_name}")
    public static String CLOUD_NAME;

    @Value("${cloudinary.api_key}")
    public static String CLOUD_API_KEY;

    @Value("${cloudinary.secret_key}")
    public static String CLOUD_SECRET_KEY;

}

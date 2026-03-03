package com.e_cormerce.shoppe.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

public class Constants {
    @Value("${cloudinary.cloud_name}")
    public String CLOUD_NAME;

    @Value("${cloudinary.api_key}")
    public String CLOUD_API_KEY;

    @Value("${cloudinary.secret_key}")
    public String CLOUD_SECRET_KEY;

    @Value("${jwt.secret_access_token}")
    public String SECRET_ACCESS_TOKEN;

    @Value("${jwt.exp_access_token}")
    public long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.secret_refresh_token}")
    public String SECRET_REFRESH_TOKEN;

    @Value("${jwt.exp_refresh_token}")
    public long REFRESH_TOKEN_EXPIRATION_TIME;
}

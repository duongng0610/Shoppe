package com.e_cormerce.shoppe.projection.category;

import java.time.LocalDate;

public interface CategoryDailyProjection {
    String getCategoryId();

    LocalDate getDate();

    Integer getTotalNewProducts();

    Integer getTotalSearches();

}

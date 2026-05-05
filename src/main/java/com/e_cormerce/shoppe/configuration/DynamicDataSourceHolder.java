package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.enums.DataSourceType;

public class DynamicDataSourceHolder {
    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static void putDataSource(DataSourceType type) {
        holder.set(type.getType());
    }

    public static String getDataSource() {
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }
}

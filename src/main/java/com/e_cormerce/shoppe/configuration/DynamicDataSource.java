package com.e_cormerce.shoppe.configuration;


import com.e_cormerce.shoppe.enums.DataSourceType;
import io.micrometer.common.lang.Nullable;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected @Nullable Object determineCurrentLookupKey() {
        System.out.println(DynamicDataSourceHolder.getDataSource());
        if (DynamicDataSourceHolder.getDataSource() == null) {
            return DataSourceType.MASTER.getType();
        }
        return DynamicDataSourceHolder.getDataSource();
    }
}

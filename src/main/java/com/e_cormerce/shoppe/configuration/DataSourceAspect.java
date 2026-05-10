package com.e_cormerce.shoppe.configuration;


import com.e_cormerce.shoppe.annotation.DataSourceAnnotation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class DataSourceAspect {

    @Before(value = "@annotation(dataSource)")
    public void setDataSource(DataSourceAnnotation dataSourceAnnotation) {
        System.out.println("Set DataSource: " + dataSourceAnnotation.value());
        DynamicDataSourceHolder.putDataSource(dataSourceAnnotation.value());
    }

    @After(value = "@annotation(dataSource)")
    public void clearDataSource() {
        System.out.println("Clear DataSource");
        DynamicDataSourceHolder.clearDataSource();
    }
}

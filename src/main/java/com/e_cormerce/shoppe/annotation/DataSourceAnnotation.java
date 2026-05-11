package com.e_cormerce.shoppe.annotation;

import com.e_cormerce.shoppe.enums.DataSourceType;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataSourceAnnotation {
  DataSourceType value() default DataSourceType.MASTER;
}

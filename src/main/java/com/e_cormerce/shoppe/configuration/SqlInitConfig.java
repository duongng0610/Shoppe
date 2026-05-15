package com.e_cormerce.shoppe.configuration;


import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SqlInitConfig {


    public void initDatabase(
            DataSource dataSource
    ) {


        // =========================
        // FUNCTIONS / PROCEDURES
        // =========================
        ResourceDatabasePopulator functionPopulator =
                new ResourceDatabasePopulator();

        functionPopulator.setSeparator("$$");

        functionPopulator.addScript(
                new ClassPathResource("sql/functions.sql")
        );

        functionPopulator.addScript(
                new ClassPathResource("sql/procedures.sql")
        );

        functionPopulator.addScript(
                new ClassPathResource("sql/triggers.sql")
        );

        DatabasePopulatorUtils.execute(
                functionPopulator,
                dataSource
        );
        // =========================
        // VIEWS
        // =========================
        ResourceDatabasePopulator viewPopulator =
                new ResourceDatabasePopulator();

        ResourceDatabasePopulator dataPopulator =
                new ResourceDatabasePopulator();

        viewPopulator.setSeparator(";");

        dataPopulator.setSeparator(";");

        viewPopulator.addScript(
                new ClassPathResource("sql/views.sql")
        );

        dataPopulator.addScript(
                new ClassPathResource("sql/enums.sql")
        );

        DatabasePopulatorUtils.execute(
                viewPopulator,
                dataSource
        );

        DatabasePopulatorUtils.execute(
                dataPopulator,
                dataSource
        );

    }

    ;

}
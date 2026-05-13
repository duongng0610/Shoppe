package com.e_cormerce.shoppe.configuration;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class SqlInitConfig {

    @Bean
    public ApplicationRunner initDatabase(
            @Qualifier("masterDataSource") DataSource dataSource
    ) {

        return args -> {

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
        };
    }
}
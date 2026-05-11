package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.enums.DataSourceType;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Profile("!test")
public class DataSourceConfig {
  @Bean
  @ConfigurationProperties(prefix = "spring.master.datasource")
  public HikariDataSource masterDataSource() {
    return new HikariDataSource();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.slave.datasource")
  public HikariDataSource slaveDataSource() {
    return new HikariDataSource();
  }

  @Bean
  public DataSource routingDataSource(
      @Qualifier("masterDataSource") DataSource masterDataSource,
      @Qualifier("slaveDataSource") DataSource slaveDataSource) {
    DynamicDataSource dataSource = new DynamicDataSource();
    Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
    targetDataSources.put(DataSourceType.MASTER.getType(), masterDataSource);
    targetDataSources.put(DataSourceType.SLAVE.getType(), slaveDataSource);
    dataSource.setTargetDataSources(targetDataSources);
    //        dataSource.setDefaultTargetDataSource(masterDataSource);
    return dataSource;
  }

  @Primary
  @Bean
  public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource) {
      @Override
      public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();

        return conn;
      }
    };
  }
}

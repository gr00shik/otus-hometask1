package ru.otus.socialnetwork.config;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean("masterDataSource")
    public DataSource masterDataSource(
            @Value("${spring.datasource.master.url}") String url,
            @Value("${spring.datasource.master.username}") String username,
            @Value("${spring.datasource.master.password}") String password) {
        var ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean("slaveDataSource")
    public DataSource slaveDataSource(
            @Value("${spring.datasource.slave.url}") String url,
            @Value("${spring.datasource.slave.username}") String username,
            @Value("${spring.datasource.slave.password}") String password) {
        var ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean
    @Primary
    public DataSource dataSource(
            @Qualifier("masterDataSource") DataSource master,
            @Qualifier("slaveDataSource") DataSource slave) {
        var routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(Map.of(
                "master", master,
                "slave", slave
        ));
        routingDataSource.setDefaultTargetDataSource(master);
        return routingDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(@Qualifier("masterDataSource") DataSource masterDataSource) {
        return Flyway.configure()
                .dataSource(masterDataSource)
                .locations("classpath:db/migration")
                .load();
    }
}

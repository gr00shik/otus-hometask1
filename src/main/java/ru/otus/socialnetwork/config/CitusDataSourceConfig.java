package ru.otus.socialnetwork.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CitusDataSourceConfig {

    @Bean("citusDataSource")
    public DataSource citusDataSource(
            @Value("${spring.datasource.citus.url}") String url,
            @Value("${spring.datasource.citus.username}") String username,
            @Value("${spring.datasource.citus.password}") String password) {
        var ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    @Bean("citusJdbcTemplate")
    public JdbcTemplate citusJdbcTemplate(@Qualifier("citusDataSource") DataSource citusDataSource) {
        return new JdbcTemplate(citusDataSource);
    }

}

package com.ids.infrastructure.config.datasources;

import com.ids.infrastructure.config.properties.UsersDbProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(UsersDbProperties.class)
@RequiredArgsConstructor
public class UsersDataSource {

    public static final String DS_BEAN = "usersDbDataSource";

    private final UsersDbProperties usersDbProperties;

    @Bean(name = DS_BEAN)
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(usersDbProperties.driverName())
                .url(usersDbProperties.url())
                .username(usersDbProperties.username())
                .password(usersDbProperties.password())
                .build();
    }
}
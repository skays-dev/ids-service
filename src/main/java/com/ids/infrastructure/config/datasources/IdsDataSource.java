package com.ids.infrastructure.config.datasources;

import com.ids.infrastructure.config.properties.IdsDbProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(IdsDbProperties.class)
@RequiredArgsConstructor
public class IdsDataSource {

    public static final String DS_BEAN = "idsDbDataSource";

    private final IdsDbProperties idsDbProperties;

    @Primary
    @Bean(name = DS_BEAN)
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(idsDbProperties.driverName())
                .url(idsDbProperties.url())
                .username(idsDbProperties.username())
                .password(idsDbProperties.password())
                .build();
    }
}
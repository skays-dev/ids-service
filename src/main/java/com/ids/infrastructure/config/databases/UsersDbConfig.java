package com.ids.infrastructure.config.databases;

import com.ids.infrastructure.config.datasources.UsersDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "com.ids.infrastructure.persistence.repository.usr"
        },
        entityManagerFactoryRef = UsersDbConfig.EMF_BEAN,
        transactionManagerRef = UsersDbConfig.TX_BEAN
)
public class UsersDbConfig {

    public static final String EMF_BEAN = "usersEntityManagerFactory";
    public static final String TX_BEAN = "usersTransactionManager";

    public static final String JDBC_BEAN = "usersJdbcTemplate";
    public static final String NAMED_JDBC_BEAN = "usersNamedParameterJdbcTemplate";

    private static final String[] ENTITY_PACKAGES = {
            "com.ids.infrastructure.persistence.entity.usr"
    };

    @Bean(name = NAMED_JDBC_BEAN)
    public NamedParameterJdbcTemplate namedJdbcTemplate(
            @Qualifier(UsersDataSource.DS_BEAN) DataSource dataSource
    ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = JDBC_BEAN)
    public JdbcTemplate jdbcTemplate(
            @Qualifier(UsersDataSource.DS_BEAN) DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = EMF_BEAN)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(UsersDataSource.DS_BEAN) DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages(ENTITY_PACKAGES)
                .properties(buildJpaProperties())
                .persistenceUnit("UsersPU")
                .build();
    }

    @Bean(name = TX_BEAN)
    public PlatformTransactionManager transactionManager(
            @Qualifier(EMF_BEAN) EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    private static Map<String, Object> buildJpaProperties() {
        return Map.ofEntries(
                Map.entry("hibernate.hbm2ddl.auto", "none"),
                Map.entry("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect"),
                Map.entry("hibernate.boot.allow_jdbc_metadata_access", "false"),
                Map.entry("hibernate.show_sql", "false"),
                Map.entry("hibernate.format_sql", "false"),
                Map.entry("hibernate.use_sql_comments", "false"),
                Map.entry("hibernate.generate_statistics", "false"),
                Map.entry("hibernate.cache.use_second_level_cache", "false"),
                Map.entry("hibernate.cache.use_query_cache", "false")
        );
    }
}
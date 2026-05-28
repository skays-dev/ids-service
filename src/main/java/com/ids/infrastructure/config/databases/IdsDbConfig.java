package com.ids.infrastructure.config.databases;

import com.ids.infrastructure.config.datasources.IdsDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
                "com.ids.infrastructure.persistence.repository.ids"
        },
        entityManagerFactoryRef = IdsDbConfig.EMF_BEAN,
        transactionManagerRef = IdsDbConfig.TX_BEAN
)
public class IdsDbConfig {

    public static final String EMF_BEAN = "idsEntityManagerFactory";
    public static final String TX_BEAN = "idsTransactionManager";

    public static final String JDBC_BEAN = "idsJdbcTemplate";
    public static final String NAMED_JDBC_BEAN = "idsNamedParameterJdbcTemplate";

    private static final String[] ENTITY_PACKAGES = {
            "com.ids.infrastructure.persistence.entity.ids"
    };

    private static final Map<String, Object> JPA_PROPERTIES = buildJpaProperties();

    @Bean(name = NAMED_JDBC_BEAN)
    public NamedParameterJdbcTemplate namedJdbcTemplate(
            @Qualifier(IdsDataSource.DS_BEAN) DataSource dataSource
    ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = JDBC_BEAN)
    public JdbcTemplate jdbcTemplate(
            @Qualifier(IdsDataSource.DS_BEAN) DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = EMF_BEAN)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(IdsDataSource.DS_BEAN) DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages(ENTITY_PACKAGES)
                .properties(JPA_PROPERTIES)
                .persistenceUnit("IdsPU")
                .build();
    }

    @Primary
    @Bean(name = {TX_BEAN, "transactionManager"})
    public PlatformTransactionManager transactionManager(
            @Qualifier(EMF_BEAN) EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    private static Map<String, Object> buildJpaProperties() {
        return Map.ofEntries(
                Map.entry("hibernate.hbm2ddl.auto", "none"),

                // Use MariaDBDialect if your hosting DB is MariaDB
                Map.entry("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect"),

                // Prevent Hibernate from querying broken/incompatible JDBC metadata
                Map.entry("hibernate.boot.allow_jdbc_metadata_access", "false"),

                Map.entry("hibernate.show_sql", "false"),
                Map.entry("hibernate.format_sql", "false"),
                Map.entry("hibernate.use_sql_comments", "false"),
                Map.entry("hibernate.generate_statistics", "false"),

                Map.entry("hibernate.jdbc.batch_size", "50"),
                Map.entry("hibernate.order_inserts", "true"),
                Map.entry("hibernate.order_updates", "true"),

                Map.entry("hibernate.cache.use_second_level_cache", "false"),
                Map.entry("hibernate.cache.use_query_cache", "false"),

                Map.entry("hibernate.connection.provider_disables_autocommit", "false"),
                Map.entry("hibernate.query.in_clause_parameter_padding", "true")
        );
    }
}
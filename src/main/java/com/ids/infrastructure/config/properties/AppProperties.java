package com.ids.infrastructure.config.properties;

import com.ids.shared.util.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@EnableConfigurationProperties({
        AppProperties.class
})
@PropertySource(
        value = "classpath:app.yml",
        ignoreResourceNotFound = true,
        factory = YamlPropertySourceFactory.class
)
public record AppProperties(
        String env,
        String appName,
        String appNumber,
        String appCode
) {
}

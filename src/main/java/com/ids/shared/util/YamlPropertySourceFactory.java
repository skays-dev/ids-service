package com.ids.shared.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Properties;

public final class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource encodedResource) throws IOException {
        Resource resource = encodedResource.getResource();

        if (!resource.exists()) {
            throw new IOException("YAML resource does not exist: " + resource.getDescription());
        }

        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource);
            factory.afterPropertiesSet();

            Properties props = factory.getObject();
            if (props == null) {
                props = new Properties();
            }

            String sourceName = (name != null && !name.isBlank())
                    ? name
                    : (resource.getFilename() != null ? resource.getFilename() : resource.getDescription());

            return new PropertiesPropertySource(sourceName, props);

        } catch (Exception ex) {
            throw new IOException("Failed to load YAML properties from: " + resource.getDescription(), ex);
        }
    }
}
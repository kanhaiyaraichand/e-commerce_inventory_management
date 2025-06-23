package com.example.ecommerce.inventory.management.ecommerce.inventory.management.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Spring Boot auto-configures Redis cache if you add dependencies & set properties.
}

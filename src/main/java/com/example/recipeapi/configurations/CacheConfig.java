package com.example.recipeapi.configurations;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    Config config() {
        Config config = new Config();
        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(3600);
        config.getMapConfigs().put("recipeCache", mapConfig);
        config.getMapConfigs().put("recipeListCache", mapConfig);
        config.getMapConfigs().put("reviewCache", mapConfig);
        config.getMapConfigs().put("reviewListCache", mapConfig);
        return config;
    }
}

package com.tree.tree.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.tree.tree.player.repository",
        entityOperationsRef = "playerR2dbcEntityTemplate"
)
public class PlayerDBConfig {

    @Value("${player-db.host}")
    private String host;

    @Value("${player-db.port}")
    private int port;

    @Value("${player-db.database}")
    private String database;

    @Value("${player-db.username}")
    private String username;

    @Value("${player-db.password}")
    private String password;

    @Value("${spring.r2dbc.pool.max-size:10}")
    private int maxSize;

    @Value("${spring.r2dbc.pool.initial-size:2}")
    private int initialSize;

    private final R2dbcConfig r2dbcConfig;

    public PlayerDBConfig(R2dbcConfig r2dbcConfig) {
        this.r2dbcConfig = r2dbcConfig;
    }

    @Bean
    public ConnectionFactory playerDataSource() {
        return r2dbcConfig.createConnectionFactory(host, port, database, username, password, maxSize, initialSize);
    }

    @Bean
    public R2dbcEntityTemplate playerR2dbcEntityTemplate(@Qualifier("playerDataSource") ConnectionFactory playerDataSource) {
        return new R2dbcEntityTemplate(playerDataSource);
    }
}

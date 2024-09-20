package com.tree.tree.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.tree.tree.ranking.repository",
        entityOperationsRef = "rankingR2dbcEntityTemplate"
)
public class RankingDBConfig {

    @Value("${ranking-db.host}")
    private String host;

    @Value("${ranking-db.port}")
    private int port;

    @Value("${ranking-db.database}")
    private String database;

    @Value("${ranking-db.username}")
    private String username;

    @Value("${ranking-db.password}")
    private String password;

    @Value("${spring.r2dbc.pool.max-size:10}")
    private int maxSize;

    @Value("${spring.r2dbc.pool.initial-size:2}")
    private int initialSize;

    private final R2dbcConfig r2dbcConfig;

    public RankingDBConfig(R2dbcConfig r2dbcConfig) {
        this.r2dbcConfig = r2dbcConfig;
    }

    @Bean
    public ConnectionFactory rankingDataSource() {
        return r2dbcConfig.createConnectionFactory(host, port, database, username, password, maxSize, initialSize);
    }

    @Bean
    public R2dbcEntityTemplate rankingR2dbcEntityTemplate(@Qualifier("rankingDataSource") ConnectionFactory rankingDataSource) {
        return new R2dbcEntityTemplate(rankingDataSource);
    }
}

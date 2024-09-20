package com.tree.tree.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

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

    @Bean
    public ConnectionFactory rankingDataSource() {
        ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, host)
                .option(PORT, port)
                .option(DATABASE, database)
                .option(USER, username)
                .option(PASSWORD, password)
                .build();

        ConnectionFactory connectionFactory = ConnectionFactories.get(baseOptions);

        ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMinutes(30))
                .maxSize(maxSize)
                .initialSize(initialSize)
                .build();

        return new ConnectionPool(poolConfig);
    }

    @Bean
    public R2dbcEntityTemplate rankingR2dbcEntityTemplate(@Qualifier("rankingDataSource") ConnectionFactory rankingDataSource) {
        return new R2dbcEntityTemplate(rankingDataSource);
    }
}

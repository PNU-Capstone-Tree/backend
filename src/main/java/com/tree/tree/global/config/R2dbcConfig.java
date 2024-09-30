package com.tree.tree.global.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Component
public class R2dbcConfig {
    public ConnectionFactory createConnectionFactory(String host, int port, String database,
                                                     String username, String password, int maxSize, int initialSize) {
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
}

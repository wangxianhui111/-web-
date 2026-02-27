package com.company.scaffold.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;
    private final JdbcTemplate jdbcTemplate;

    public CustomHealthIndicator(DataSource dataSource, RedisConnectionFactory redisConnectionFactory, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.redisConnectionFactory = redisConnectionFactory;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        Health.Builder builder = Health.up();

        // 检查数据库连接
        try {
            jdbcTemplate.execute("SELECT 1");
            builder.withDetail("database", "UP");
        } catch (Exception e) {
            log.error("Database health check failed", e);
            builder.withDetail("database", "DOWN");
            builder.down(e);
        }

        // 检查Redis连接
        try {
            redisConnectionFactory.getConnection().ping();
            builder.withDetail("redis", "UP");
        } catch (Exception e) {
            log.error("Redis health check failed", e);
            builder.withDetail("redis", "DOWN");
            builder.down(e);
        }

        return builder.build();
    }
}

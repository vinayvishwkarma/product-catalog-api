package com.learning.product_catalog_api.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ping controller to check health of the api.
 */
@RestController
@RequestMapping("/api/v1/ping")
public class PingController {

    private final DataSource dataSource;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public PingController(DataSource dataSource, StringRedisTemplate redisTemplate) {
        this.dataSource = dataSource;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> healthStatus = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                healthStatus.put("database", "UP");
            } else {
                healthStatus.put("database", "DOWN");
            }
        } catch (Exception e) {
            healthStatus.put("database", "DOWN");
        }
        try {
            String redisPing = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equalsIgnoreCase(redisPing)) {
                healthStatus.put("redis", "UP");
            } else {
                healthStatus.put("redis", "DOWN");
            }
        } catch (Exception e) {
            healthStatus.put("redis", "DOWN");
        }
        return ResponseEntity.ok(healthStatus);
    }
}

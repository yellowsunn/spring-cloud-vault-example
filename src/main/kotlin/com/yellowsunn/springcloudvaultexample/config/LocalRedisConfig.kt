package com.yellowsunn.springcloudvaultexample.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import redis.embedded.RedisServer

@Profile("local")
@Configuration
class LocalRedisConfig {

    private companion object {
        private const val REDIS_PORT = 63790
    }

    private val redisServer: RedisServer = RedisServer.builder()
        .port(REDIS_PORT)
        .setting("maxmemory 100mb")
        .setting("maxmemory-policy allkeys-lru")
        .build()

    @PostConstruct
    fun start() {
        redisServer.start()
    }

    @PreDestroy
    fun stop() {
        redisServer.stop()
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory("localhost", REDIS_PORT)
    }

    @Bean
    fun stringRedisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
    ): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory)
    }
}

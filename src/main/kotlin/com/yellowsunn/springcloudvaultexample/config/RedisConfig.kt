package com.yellowsunn.springcloudvaultexample.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
@Profile("!local")
class RedisConfig {
    @Bean
    fun stringRedisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
    ): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory)
    }
}

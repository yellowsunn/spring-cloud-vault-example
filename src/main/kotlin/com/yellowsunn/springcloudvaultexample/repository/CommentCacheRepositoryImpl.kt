package com.yellowsunn.springcloudvaultexample.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.springcloudvaultexample.dto.CommentDto
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CommentCacheRepositoryImpl(
    private val stringRedisTemplate: StringRedisTemplate,
) : CommentCacheRepository {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    override fun findById(id: Long): CommentDto? {
        return try {
            val data: String? = stringRedisTemplate.opsForValue()[generateKey(id)]
            data?.let {
                objectMapper.readValue(it, CommentDto::class.java)
            }
        } catch (e: Exception) {
            logger.error("Failed to get cached comment. message={}", e.message, e)
            null
        }
    }

    override fun save(comment: CommentDto) {
        try {
            val data: String = objectMapper.writeValueAsString(comment)
            val operations = stringRedisTemplate.opsForValue()
            operations[generateKey(comment.id), data] = Duration.ofMinutes(10L)

            logger.info("Cache a comment. id = {}", comment.id)
        } catch (e: Exception) {
            logger.error("Failed to set cached comment. message={}", e.message, e)
        }
    }

    private fun generateKey(commentId: Long): String {
        return "comments:$commentId"
    }
}

package com.yellowsunn.springcloudvaultexample.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yellowsunn.springcloudvaultexample.dto.CommentDto
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CommentCacheRepositoryImpl(
    private val stringRedisTemplate: StringRedisTemplate,
) : CommentCacheRepository {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    override fun findById(id: Long): CommentDto? {
        val data: String? = stringRedisTemplate.opsForValue()[generateKey(id)]
        return data?.let {
            objectMapper.readValue(it, CommentDto::class.java)
        }
    }

    override fun save(comment: CommentDto) {
        val data: String = objectMapper.writeValueAsString(comment)
        val operations = stringRedisTemplate.opsForValue()
        operations[generateKey(comment.id), data] = Duration.ofMinutes(10L)
    }

    private fun generateKey(commentId: Long): String {
        return "comments:$commentId"
    }
}

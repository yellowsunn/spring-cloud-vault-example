package com.yellowsunn.springcloudvaultexample.service

import com.yellowsunn.springcloudvaultexample.dto.CommentDto
import com.yellowsunn.springcloudvaultexample.repository.CommentCacheRepository
import com.yellowsunn.springcloudvaultexample.repository.CommentRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val commentCacheRepository: CommentCacheRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getComment(id: Long): CommentDto {
        val cachedComment: CommentDto? = commentCacheRepository.findById(id)
        if (cachedComment != null) {
            return cachedComment
        }

        val comment = commentRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("댓글을 찾을 수 없습니다.")

        return CommentDto(
            id = comment.id,
            postId = comment.postId,
            email = comment.email,
            name = comment.name,
            body = comment.body,
        ).also {
            commentCacheRepository.save(it)
        }
    }
}

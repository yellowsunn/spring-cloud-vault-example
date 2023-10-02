package com.yellowsunn.springcloudvaultexample.repository

import com.yellowsunn.springcloudvaultexample.dto.CommentDto

interface CommentCacheRepository {

    fun findById(id: Long): CommentDto?

    fun save(comment: CommentDto)
}

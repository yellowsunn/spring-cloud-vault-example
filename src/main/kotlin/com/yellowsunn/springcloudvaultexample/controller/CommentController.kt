package com.yellowsunn.springcloudvaultexample.controller

import com.yellowsunn.springcloudvaultexample.dto.CommentDto
import com.yellowsunn.springcloudvaultexample.service.CommentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
    private val commentService: CommentService,
) {

    @GetMapping("/api/v1/comments/{commentId}")
    fun getComment(@PathVariable commentId: Long): CommentDto {
        return commentService.getComment(commentId)
    }
}

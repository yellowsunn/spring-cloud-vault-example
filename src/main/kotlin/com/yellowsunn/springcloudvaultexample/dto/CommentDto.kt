package com.yellowsunn.springcloudvaultexample.dto

data class CommentDto(
    val id: Long,
    val postId: Long,
    val email: String,
    val name: String,
    val body: String,
)

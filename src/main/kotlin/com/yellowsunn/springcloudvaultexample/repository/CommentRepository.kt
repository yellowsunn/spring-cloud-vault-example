package com.yellowsunn.springcloudvaultexample.repository

import com.yellowsunn.springcloudvaultexample.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>

package com.example.bsu_contest.models

import java.util.Date

data class Comment(
    val id: Int,
    val created_at: Date,
    val updated_at: Date,
    val user_id: Int,
    val content: String,
    val status: Int,
    val report_id: Int,
    val author: Author
)

package com.example.bsu_contest.models

import java.util.Date

data class Author (
    val team_name: String,
    val description: String,
    val track_id: Int,
    val track_name: String,
    val track_description: String,
    val user_name: String,
    val updated_at: Date
)

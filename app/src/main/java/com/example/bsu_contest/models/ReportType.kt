package com.example.bsu_contest.models

import java.util.Date

data class ReportType (
    val id: Int,
    val created_at: Date,
    val updated_at: Date,
    val title: String,
    val description: String,
    val tag: String,
)
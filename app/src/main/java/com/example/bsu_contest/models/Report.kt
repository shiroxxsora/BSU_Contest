package com.example.bsu_contest.models

data class Report(
    val id: Int,
    val title: String,
    val content: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val img_link: String,
    val author: Author,
    val report_type: ReportType
)
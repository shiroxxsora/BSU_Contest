package com.example.bsu_contest.models

import java.util.Date

data class Report(
    val id: Int,
    val created_at: Date,
    val updated_at: Date,
    val title: String,
    val content: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val img_link: String,
    val author: Author,
    val report_type: ReportType
)

data class SendingReport(
    val title: String,
    val report_type_id: Int,
    val content: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val img_link: String,
)

data class EditingReport(
    val report_id: Int,
    val title: String,
    val report_type_id: Int,
    val content: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
)
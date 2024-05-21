package com.example.bsu_contest.models

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface MainApi {
    @GET("reports/{id}")
    suspend fun getReportById(@Path("id") id: Int): ReportData

    @GET("reports/all")
    suspend fun getAllReports(): ReportDataList

    @Headers("Content-Type: application/json")
    @GET("reports/get/all")
    suspend fun getAllReportsOfUser(@Header("Authorization") token: String)

    @Headers("Content-Type: application/json")
    @PUT("reports/add")
    suspend fun addReport(@Header("Authorization") token: String): ReportData

    @GET("comments/{id}")
    suspend fun getPublicCommentsByReportId(@Path("id") id: Int): CommentDataList

    @Headers("Content-Type: application/json")
    @GET("comments/{id}")
    suspend fun getAllCommentsByReportId(@Header("Authorization") token: String, @Path("id") id: Int): CommentDataList

    @Headers("Content-Type: application/json")
    @GET("comments/get/all")
    suspend fun getAllCommentsOfUser(@Header("Authorization") token: String): CommentDataList

    @GET("comments/delete/{id}}")
    suspend fun deleteCommentById(@Header("Authorization") token: String, @Path("id") id: Int)

    @Headers("Content-Type: application/json")
    @PUT("comments/add")
    suspend fun addComment(@Header("Authorization") token: String, @Body comment:SendingComment): CommentData
}
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
    suspend fun getAllReportsOfUser(@Header("Authorization") token: String): ReportDataList

    @Headers("Content-Type: application/json")
    @PUT("reports/add")
    suspend fun addReport(@Header("Authorization") token: String, @Body report:SendingReport): ReportData
    @GET("reports/delete/{id}}")
    suspend fun deleteReportById(@Header("Authorization") token: String, @Path("id") id: Int)

    @Headers("Content-Type: application/json")
    @PUT("reports/edit")
    suspend fun editReport(@Header("Authorization") token: String, @Body report:EditingReport)

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

    @Headers("Content-Type: application/json")
    @PUT("comments/edit")
    suspend fun editComment(@Header("Authorization") token: String, @Body comment:EditingComment)
}
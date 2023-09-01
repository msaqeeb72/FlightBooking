package com.saqeeb.testing.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FlightAPI {
    @POST("/api/login")
    suspend fun login(@Body request: String): Response<String>
}
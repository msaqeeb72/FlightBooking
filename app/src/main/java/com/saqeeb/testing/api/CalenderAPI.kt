package com.saqeeb.testing.api

import com.saqeeb.testing.models.CalenderResponse
import com.saqeeb.testing.models.GroupsByCountryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CalenderAPI {
    @GET("/api/flight/calendar?")
    suspend fun fetchCalender(@Query("From") from: String,@Query("To") to: String): Response<CalenderResponse>
}
package com.saqeeb.testing.api

import com.saqeeb.testing.models.GroupsByCountryResponse
import com.saqeeb.testing.utils.NetworkResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FlightAPI {
    @GET("/ibe//search/portGroupsByCountry/{airport}")
    suspend fun portGroupsByCountry(@Path("airport") airport: String): Response<GroupsByCountryResponse>
}
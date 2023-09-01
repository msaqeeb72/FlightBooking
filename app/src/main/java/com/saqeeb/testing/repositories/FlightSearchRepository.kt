package com.saqeeb.testing.repositories

import com.saqeeb.testing.api.FlightAPI
import javax.inject.Inject

class FlightSearchRepository @Inject constructor( private val flightAPI: FlightAPI) {
}
package com.saqeeb.testing.models

data class CityAirport(
    val city: String?=null,
    val cityName: String?=null,
    val code: String?=null,
    val country: String?=null,
    val countryName: String?=null,
    val displayText: String?=null,
    val image: Any?=null,
    val portName: String?=null,
    val timeZone: Any?=null
){
    override fun toString(): String {
        return displayText?:""
    }
}
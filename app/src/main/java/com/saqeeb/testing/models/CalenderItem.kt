package com.saqeeb.testing.models

import com.google.gson.annotations.SerializedName

data class CalenderItem(
    val DAY: String,
    val From: String,
    val To: String,
    @SerializedName("_id")
    val id: String
)
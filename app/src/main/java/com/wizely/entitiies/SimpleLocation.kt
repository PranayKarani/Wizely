package com.wizely.entitiies

import com.google.gson.annotations.SerializedName
import java.util.*

data class SimpleLocation(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)
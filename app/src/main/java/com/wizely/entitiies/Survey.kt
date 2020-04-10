package com.wizely.entitiies

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class Survey(
    @SerializedName("name")
    val name: String) {

    @SerializedName("locations")
    val locations = ArrayList<SimpleLocation>()

    @SerializedName("area")
    var area: Double = 0.0

    override fun toString(): String {
        return "name: $name, locations: ${locations.size}"
    }

}
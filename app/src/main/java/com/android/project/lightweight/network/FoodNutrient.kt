package com.android.project.lightweight.network

import com.squareup.moshi.Json

data class FoodNutrient(
    val nutrientName : String,
    val unitName : String,
    @Json(name = "value")
    val amount : Double
)

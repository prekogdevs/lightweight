package com.android.project.lightweight.api.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodNutrient(
    val nutrientName : String,
    val unitName : String,
    @Json(name = "value")
    val amount : Double, // amount in 100 "unitName" e.g.: 100 g, mg etc.
    val nutrientNumber : Double
) : Parcelable
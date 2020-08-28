package com.android.project.lightweight.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodNutrient(
    val nutrientName : String,
    val unitName : String,
    @Json(name = "value")
    val amount : Double,
    val nutrientNumber : Int
) : Parcelable

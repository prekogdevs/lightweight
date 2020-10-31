package com.android.project.lightweight.api.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    @Json(name = "fdcId")
    val id: Long,
    val description: String,
    val foodNutrients: List<FoodNutrient>
) : Parcelable
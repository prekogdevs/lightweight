package com.android.project.lightweight.api.retrofit.model

import  android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class FoodNutrient(
    val nutrientName: String,
    val unitName: String,
    @SerializedName("value")
    val amount: Double, // amount in 100 "unitName" e.g.: 100 g, mg etc.
    val nutrientNumber: Double
) : Parcelable
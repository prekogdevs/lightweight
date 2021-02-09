package com.android.project.lightweight.api.retrofit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    val fdcId: Long,
    val description: String,
    val foodNutrients: List<FoodNutrient>
) : Parcelable
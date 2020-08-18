package com.android.project.lightweight.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    val fdcId: Long,
    val description: String,
    val foodNutrients : List<FoodNutrient>
) : Parcelable

package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "food", primaryKeys = ["consumedOn", "id"])
data class Food(
    @Json(name = "fdcId")
    val id: Long,
    val description: String,
    val foodNutrients: List<FoodNutrient>,
    var consumedOn: Long = -1L
) : Parcelable
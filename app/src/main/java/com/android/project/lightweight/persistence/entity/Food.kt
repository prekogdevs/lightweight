package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "food", primaryKeys = ["consumedWhen", "fdcId"])
data class Food(
    val fdcId: Long,
    val description: String,
    val foodNutrients: List<FoodNutrient>,
    var consumedWhen: Long = -1L
) : Parcelable
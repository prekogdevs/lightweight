package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.Entity
import com.android.project.lightweight.api.model.FoodNutrient
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Diary", primaryKeys = ["consumedOn", "id"])
data class DiaryEntry(
    @Json(name = "fdcId")
    val id: Long,
    val description: String,
    val foodNutrients: List<FoodNutrient>, // TODO: FoodNutrient from API, is it correct?
    var consumedOn: Long = -1L
) : Parcelable
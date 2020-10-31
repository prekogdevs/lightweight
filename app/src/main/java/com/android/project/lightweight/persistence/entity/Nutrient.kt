package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "nutrient")
data class Nutrient(
    @PrimaryKey
    val id: Long,
    val nutrientName : String,
    val unitName : String,
    @Json(name = "value")
    val amount : Double,
    val nutrientNumber : Int
) : Parcelable

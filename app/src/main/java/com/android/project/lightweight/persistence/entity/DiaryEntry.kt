package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Diary")
data class DiaryEntry(
    val fdcId: Long,
    val description: String,
    var consumedOn: Long,
    var consumedAmount : Int = 0,
    var unitName : String = "g",
    var kcal : Int = 0,
    var protein : Int = 0,
    var carbs : Int = 0,
    var fats: Int = 0
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @Ignore
    var nutrients : List<NutrientEntry> = emptyList()
}
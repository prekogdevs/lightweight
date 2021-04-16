package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Diary")
data class DiaryEntry constructor(
    val fdcId: Long = 0,
    val description: String = "",
    val consumedOn: Long = 0,
    var consumedAmount: Int = 0, // Consumed amount in grams e.g.: 150g banana
    var consumedCalories: Double = 0.0
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    @IgnoredOnParcel
    var nutrientEntries: List<NutrientEntry> = emptyList()
}
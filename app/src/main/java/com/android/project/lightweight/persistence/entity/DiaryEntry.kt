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
    val fdcId: Long,
    val description: String,
    var consumedOn: Long,
    var consumedAmount: Int = 0, // Consumed amount in grams e.g.: 150g banana
    var consumedKCAL: Double = 0.0,
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    @IgnoredOnParcel
    var nutrients: List<NutrientEntry> = emptyList()
}
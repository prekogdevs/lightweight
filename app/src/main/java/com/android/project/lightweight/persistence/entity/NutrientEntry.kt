package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "Nutrient", foreignKeys = [
        ForeignKey(
            entity = DiaryEntry::class,
            parentColumns = ["id"],
            childColumns = ["diaryEntryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NutrientEntry(
    @ColumnInfo(index = true)
    var diaryEntryId: Long,
    val consumedOn: Long,
    val nutrientNumber: Double,
    val originalComponentValueInPortion: Double, // component value in 100g food e.g.: 1.93mg iron in 100g banana
    val unitName: String,
    val nutrientName: String,
    var consumedAmount: Double = originalComponentValueInPortion
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
